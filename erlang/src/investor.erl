-module(investor).

%% API
-export([handle_investor/2]).

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

handle_investor(ClientSocket, Username) ->
  ExchangeMap = get_exchange_map(#{}, util:number_of_exchanges()),
  handle_investor(ClientSocket, Username, ExchangeMap).
handle_investor(ClientSocket, Username, ExchangeMap) ->
  receive
    {tcp, _, Bin} ->
      io:fwrite('~p~n', [messages:decode_msg(Bin, 'Request')]),
      case messages:decode_msg(Bin, 'Request') of
        {'Request', "LogoutRequest"} ->
          logout_investor(ClientSocket, Username, ExchangeMap, Bin);
        {'Request', "AuctionBid"} ->
          investor_bid(ClientSocket, Username, ExchangeMap, Bin);
        {'Request', "FixedSubscription"} ->
          investor_fixed(ClientSocket, Username, ExchangeMap, Bin);
        _ ->
          io:fwrite('WRONG REQUEST.\n'),
          handle_investor(ClientSocket, Username, ExchangeMap)
      end;
    {tcp_closed, _} ->
      io:fwrite('Investor terminated in handle.~n'),
      loginManager:logout(Username),
      invalid;
    {tcp_error, _, _} ->
      io:fwrite('TCP error in handle.~n'),
      loginManager:logout(Username),
      invalid
  end.

investor_fixed(ClientSocket, Username, ExchangeMap, Bin) ->
  io:fwrite('Fixed subscription request. ~p~n', [Username]),
  io:fwrite('Forwarding'),
  {_, _, _, Company, _} = messages:decode_msg(Bin, 'FixedSubscription'),
  Port = util:get_exchange(Company),
  {ok, ExchangeSocket} = maps:find(Port, ExchangeMap),
  gen_tcp:send(ExchangeSocket, Bin),
  receive
    {tcp, _, Bin} ->
      gen_tcp:send(ClientSocket, Bin),
      handle_investor(ClientSocket, Username, ExchangeMap);
    _ ->
      loginManager:logout(Username)
  end.

investor_bid(ClientSocket, Username, ExchangeMap, Bin) ->
  io:fwrite('Auction bid request. ~p~n', [Username]),
  io:fwrite('Forwarding'),
  {_, _, _, Company, _, _} = messages:decode_msg(Bin, 'AuctionBid'),
  Port = util:get_exchange(Company),
  {ok, ExchangeSocket} = maps:find(Port, ExchangeMap),
  gen_tcp:send(ExchangeSocket, Bin),
  receive
    {tcp, _, Bin} ->
      gen_tcp:send(ClientSocket, Bin),
      handle_investor(ClientSocket, Username, ExchangeMap);
    _ ->
      loginManager:logout(Username)
  end.

logout_investor(ClientSocket, Username, ExchangeMap, Bin) ->
  io:fwrite('Investor logout request. ~p~n', [Username]),
  case messages:decode_msg(Bin, 'LogoutRequest') of
    {_, _, Username} ->
      case loginManager:logout(Username) of
        ok ->
          io:fwrite('Logout ok.\n'),
          Reply = messages:encode_msg(#'Reply'{result = true, message = "Logout ok"}),
          gen_tcp:send(ClientSocket, Reply),
          authenticator:authenticate(ClientSocket);
        _ ->
          io:fwrite('Logout not ok.\n'),
          Reply = messages:encode_msg(#'Reply'{result = false, message = "Logout not ok"}),
          gen_tcp:send(ClientSocket, Reply),
          handle_investor(ClientSocket, Username, ExchangeMap)
      end;
    _ ->
      io:fwrite('Logout not ok.\n'),
      Reply = messages:encode_msg(#'Reply'{result = false, message = "Logout not ok"}),
      gen_tcp:send(ClientSocket, Reply),
      handle_investor(ClientSocket, Username, ExchangeMap)
  end.