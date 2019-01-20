-module(company).

%% API
-export([handle/2]).

%% Import
-include("messages.hrl").

%% Implementation
handle(ClientSocket, Company) ->
  {ok, ExchangeSocket} = gen_tcp:connect("localhost",
    util:get_exchange(Company), [binary, {packet, 4}, {reuseaddr, true}]),
  handle(ClientSocket, Company, ExchangeSocket).
handle(ClientSocket, Company, ExchangeSocket) ->
  receive
    {tcp, _, Bin} ->
      io:fwrite("~p~n", [messages:decode_msg(Bin, 'Request')]),
      case messages:decode_msg(Bin, 'Request') of
        {'Request', "Auction"} ->
          io:fwrite("Auction creation request. ~p~n", [Company]),
          request_reply(ClientSocket, Company, ExchangeSocket, Bin);
        {'Request', "FixedLoan"} ->
          io:fwrite("Fixed loan creation request. ~p~n", [Company]),
          request_reply(ClientSocket, Company, ExchangeSocket, Bin);
        {'Request', "CompanyInfoAuctionRequest"} ->
          io:fwrite("Company info auction request. ~p~n", [Company]),
          request_reply(ClientSocket, Company, ExchangeSocket, Bin);
        {'Request', "CompanyInfoFixedRequest"} ->
          io:fwrite("Company info fixed request. ~p~n", [Company]),
          request_reply(ClientSocket, Company, ExchangeSocket, Bin);
        _ -> authenticator:logout(Company)
      end;
    _ -> authenticator:logout(Company)
  end.

request_reply(ClientSocket, Company, ExchangeSocket, Bin) ->
  io:fwrite("Forwarding.~n"),
  gen_tcp:send(ExchangeSocket, Bin),
  receive
    {tcp, _, Reply} ->
      io:fwrite("~p~n", [messages:decode_msg(Reply,'Reply')]),
      gen_tcp:send(ClientSocket, Reply),
      handle(ClientSocket, Company, ExchangeSocket)
    after (60 * 1000) -> authenticator:logout(Company)
  end.