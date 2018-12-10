-module(server).

%% API
-export([main/0]).

%% Import
-include("messages.hrl").

%% Implementation
main() ->
  loginManager:create(),
  loginManager:create_account(<<"diogo">>, <<"diogo">>),
  loginManager:create_account(<<"salete">>, <<"salete">>),
  loginManager:create_account(<<"sofia">>, <<"sofia">>),
  {ok, ServerSocket} = gen_tcp:listen(11111, [binary, {packet, 4}, {reuseaddr, true}]),
  acceptor(ServerSocket).

acceptor(ServerSocket) ->
  {ok, Socket} = gen_tcp:accept(ServerSocket),
  io:fwrite('Connection accepted.\n'),
  gen_tcp:controlling_process(Socket, spawn(fun() -> authenticate(Socket) end)),
  acceptor(ServerSocket).

authenticate(Socket) ->
  receive
    {tcp, _, Bin} ->
      io:format("~p\n", [messages:decode_msg(Bin, 'Login')]),
      io:format("received\n");
    {tcp, _, _} ->
      io:fwrite('wrong'),
      gen_tcp:send(Socket, <<"false\n">>),
      authenticate(Socket);
    {tcp_closed, _} ->
      io:fwrite('User gave up authentication.~n'),
      invalid;
    {tcp_error, _, _} ->
      io:fwrite('TCP error in authentication.~n'),
      invalid
  end.
