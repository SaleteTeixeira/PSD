-module(investor).

%% API
-export([handle/2]).

%% Import
-include("messages.hrl").

%% Implementation
get_exchange_map(ExchangeMap, Accum) ->
  N = util:number_of_exchanges() * 2,
  case Accum of
    N -> ExchangeMap;
    _ ->
      Port = util:get_port(Accum),
      {ok, Socket} = gen_tcp:connect("localhost", Port, [binary, {packet, 4}, {reuseaddr, true}]),
      UpdatedMap = maps:put(Port, Socket, ExchangeMap),
      get_exchange_map(UpdatedMap, Accum + 1)
  end.

handle(ClientSocket, Username) ->
  ExchangeMap = get_exchange_map(#{}, util:number_of_exchanges()),
  io:fwrite("~p~n", [ExchangeMap]),
  handle(ClientSocket, Username, ExchangeMap).
handle(ClientSocket, Username, ExchangeMap) ->
  receive
    {tcp, _, Bin} ->
      io:fwrite('~p~n', [messages:decode_msg(Bin, 'Request')]),
      case messages:decode_msg(Bin, 'Request') of
        {'Request', "AuctionBid"} ->
          io:fwrite("Auction bid request. ~p~n", [Username]),
          {_, _, _, Company, _, _} = messages:decode_msg(Bin, 'AuctionBid'),
          request_reply(ClientSocket, Username, ExchangeMap, Bin, Company);
        {'Request', "FixedSubscription"} ->
          io:fwrite("Fixed subscription request. ~p~n", [Username]),
          {_, _, _, Company, _} = messages:decode_msg(Bin, 'FixedSubscription'),
          request_reply(ClientSocket, Username, ExchangeMap, Bin, Company);
        {'Request', "AuctionList"} ->
          io:fwrite("Auction list request. ~p~n", [Username]),
          request_reply(ClientSocket, Username, ExchangeMap, Bin, "a");
        {'Request', "FixedList"} ->
          io:fwrite("Fixed loan list request. ~p~n", [Username]),
          request_reply(ClientSocket, Username, ExchangeMap, Bin, "a");
        {'Request', "CompanyList"} ->
          io:fwrite("Company list request. ~p~n", [Username]),
          request_reply(ClientSocket, Username, ExchangeMap, Bin, "a");
        {'Request', "CompanyInfoRequest"} ->
          io:fwrite("Company Info request. ~p~n", [Username]),
          {_, _, Company} = messages:decode_msg(Bin, 'CompanyInfoRequest'),
          request_reply(ClientSocket, Username, ExchangeMap, Bin, Company);
        _ -> authenticator:logout(Username)
      end;
    _ -> authenticator:logout(Username)
  end.

request_reply(ClientSocket, Username, ExchangeMap, Bin, Company) ->
  Port = util:get_exchange(Company),
  {ok, ExchangeSocket} = maps:find(Port, ExchangeMap),
  io:fwrite("Forwarding.~n"),
  gen_tcp:send(ExchangeSocket, Bin),
  receive
    {tcp, _, Reply} ->
      io:fwrite("~p~n", [messages:decode_msg(Reply, 'Reply')]),
      gen_tcp:send(ClientSocket, Reply),
      handle(ClientSocket, Username, ExchangeMap)
  after (60 * 1000) -> authenticator:logout(Company)
  end.