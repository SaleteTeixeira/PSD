-module(server).

%% API
-export([main/0]).

%% Import
-include("auth.hrl").

%% Implementation
main() ->
  loginManager:create(),
  loginManager:create_account("diogo", "diogo"),
  loginManager:create_account("salete", "salete"),
  loginManager:create_account("sofia", "sofia"),
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
      case auth:decode_msg(Bin, 'Request') of
        {'Request', "Login", Username, Password} ->
          io:fwrite('Login request. ~p ~p\n', [Username, Password]),
          case loginManager:login(Username, Password) of
            ok ->
              io:fwrite('Login ok.\n'),
              Reply = auth:encode_msg(#'Reply'{result = true, message = "Login ok"}),
              gen_tcp:send(Socket, Reply),
              handle(Socket, Username);
            _ ->
              io:fwrite('Login not ok.\n'),
              Reply = auth:encode_msg(#'Reply'{result = false, message = "Login not ok"}),
              gen_tcp:send(Socket, Reply),
              authenticate(Socket)
          end;
        _ ->
          io:fwrite('WRONG.\n'),
          Reply = auth:encode_msg(#'Reply'{result = false, message = "WRONG LOGIN"}),
          gen_tcp:send(Socket, Reply)
      end;
    {tcp_closed, _} ->
      io:fwrite('User gave up authentication.~n'),
      invalid;
    {tcp_error, _, _} ->
      io:fwrite('TCP error in authentication.~n'),
      invalid
  end.

handle(Socket, Username) ->
  receive
    {tcp, _, Bin} ->
      io:fwrite('~p~n', [auth:decode_msg(Bin, 'Request')]),
      case auth:decode_msg(Bin, 'Request') of
        {'Request', "Logout", Username, _} ->
          io:fwrite('Logout request. ~p\n', [Username]),
          case loginManager:logout(Username) of
            ok ->
              io:fwrite('Logout ok.\n'),
              Reply = auth:encode_msg(#'Reply'{result = true, message = "Logout ok"}),
              gen_tcp:send(Socket, Reply),
              authenticate(Socket);
            _ ->
              io:fwrite('Logout not ok.\n'),
              Reply = auth:encode_msg(#'Reply'{result = false, message = "Logout not ok"}),
              gen_tcp:send(Socket, Reply),
              handle(Socket, Username)
          end;
        _ ->
          io:fwrite('WRONG.\n'),
          Reply = auth:encode_msg(#'Reply'{result = false, message = "WRONG LOGOUT"}),
          gen_tcp:send(Socket, Reply)
      end;
    {tcp_closed, _} ->
      io:fwrite('User gave up authentication.~n'),
      invalid;
    {tcp_error, _, _} ->
      io:fwrite('TCP error in authentication.~n'),
      invalid
  end.