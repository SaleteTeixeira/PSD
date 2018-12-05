-module(server).

%% API
-export([main/0]).

%% Implementation
main() ->
  loginManager:create(),
  loginManager:create_account(<<"diogo">>, <<"diogo">>),
  loginManager:create_account(<<"salete">>, <<"salete">>),
  loginManager:create_account(<<"sofia">>, <<"sofia">>),
  {ok, ServerSocket} = gen_tcp:listen(11111, [binary, {packet, line}, {reuseaddr, true}]),
  acceptor(ServerSocket).

acceptor(ServerSocket) ->
  {ok, Socket} = gen_tcp:accept(ServerSocket),
  io:fwrite('Connection accepted.\n'),
  gen_tcp:controlling_process(Socket, spawn(fun() -> authenticate(Socket) end)),
  acceptor(ServerSocket).

authenticate(Socket) ->
  receive
    {tcp, _, <<"/login ", NameArg/binary>>} ->
      [Username, Password] = string:split(string:trim(NameArg), " "),
      io:fwrite('Login request. ~p ~p\n', [Username, Password]),
      case loginManager:login(Username, Password) of
        ok ->
          io:fwrite('Login ok.\n'),
          gen_tcp:send(Socket, <<"true\n">>),
          authenticate(Socket);
        _ ->
          io:fwrite('Login not ok.\n'),
          gen_tcp:send(Socket, <<"false\n">>),
          authenticate(Socket)
      end;
    {tcp, _, <<"/register ", NameArg/binary>>} ->
      io:fwrite('register'),
      [Username, Password] = string:split(string:trim(NameArg), " "),
      case loginManager:create_account(Username, Password) of
        ok ->
          io:fwrite('register ok\n'),
          gen_tcp:send(Socket, <<"true\n">>),
          authenticate(Socket);
        _ ->
          io:fwrite('register not ok\n'),
          gen_tcp:send(Socket, <<"false\n">>),
          authenticate(Socket)
      end;
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

temp(Socket) ->
  temp(Socket).