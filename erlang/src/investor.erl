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
      maps:put(Port, Socket, ExchangeMap),
      get_exchange_map(ExchangeMap, Accum + 1)
  end.

handle(ClientSocket, Username) ->
  ExchangeMap = get_exchange_map(#{}, util:number_of_exchanges()),
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
        _ -> authenticator:logout(Username)
      end;
    _ -> authenticator:logout(Username)
  end.

request_reply(ClientSocket, Username, ExchangeMap, Bin, Company) ->
  Port = util:get_exchange(Company),
  {ok, ExchangeSocket} = maps:find(Port, ExchangeMap),
  gen_tcp:send(ExchangeSocket, Bin),
  receive
    {tcp, _, Bin} ->
      gen_tcp:send(ClientSocket, Bin),
      handle(ClientSocket, Username, ExchangeMap);
    _ ->
      loginManager:logout(Username)
  end.