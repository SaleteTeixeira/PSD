-module(server_app).

%% API
-export([main/0]).

%% Import
-include("messages.hrl").

%% Implementation
main() ->
  loginManager:create(),
  loginManager:create_account("diogo", "diogo", "Investor"),
  loginManager:create_account("salete", "salete", "Investor"),
  loginManager:create_account("sofia", "sofia", "Investor"),
  loginManager:create_account("zara", "zara", "Company"),
  {ok, ServerSocket} = gen_tcp:listen(11111, [binary, {packet, 4}, {reuseaddr, true}]),
  acceptor(ServerSocket).

acceptor(ServerSocket) ->
  {ok, Socket} = gen_tcp:accept(ServerSocket),
  io:fwrite("Connection accepted.\n"),
  gen_tcp:controlling_process(Socket, spawn(fun() -> authenticator:authenticate(Socket) end)),
  acceptor(ServerSocket).