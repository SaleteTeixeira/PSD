-module(company).

%% API
-export([handle/2]).

%% Import
-include("messages.hrl").

%% Implementation
handle(ClientSocket, Company) ->
  %{ok, ExchangeSocket} = gen_tcp:connect("localhost",
  %  util:get_exchange(Company), [binary, {packet, 4}, {reuseaddr, true}]),
  {ok, ExchangeSocket} = gen_tcp:connect("localhost",
    12345, [binary, {packet, 4}, {reuseaddr, true}]),
  handle(ClientSocket, Company, ExchangeSocket).
handle(ClientSocket, Company, ExchangeSocket) ->
  receive
    {tcp, _, Bin} ->
      io:fwrite("~p~n", [messages:decode_msg(Bin, 'Request')]),
      case messages:decode_msg(Bin, 'Request') of
        {'Request', "Auction"} ->
          io:fwrite("Fixed loan creation request. ~p~n", [Company]),
          request_reply(ClientSocket, Company, ExchangeSocket, Bin);
        {'Request', "FixedLoan"} ->
          io:fwrite("Fixed loan creation request. ~p~n", [Company]),
          request_reply(ClientSocket, Company, ExchangeSocket, Bin);
        _ -> authenticator:logout(Company)
      end;
    _ -> authenticator:logout(Company)
  end.

request_reply(ClientSocket, Company, ExchangeSocket, Bin) ->
  io:fwrite("Forwarding.~n"),
  gen_tcp:send(ExchangeSocket, Bin),
  receive
    {tcp, _, Bin} ->
      gen_tcp:send(ClientSocket, Bin),
      handle(ClientSocket, Company, ExchangeSocket);
    _ -> authenticator:logout(Company)
  end.