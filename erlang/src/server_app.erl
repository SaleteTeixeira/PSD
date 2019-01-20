-module(server_app).

%% API
-export([main/0]).

%% Import
-include("messages.hrl").

%% Implementation
main() ->
  loginManager:create(),
  loginManager:create_account("diogo", "pass", "Investor"),
  loginManager:create_account("salete", "pass", "Investor"),
  loginManager:create_account("sofia", "pass", "Investor"),
  loginManager:create_account("Zara", "pass", "Company"),
  loginManager:create_account("Mango", "pass", "Company"),
  loginManager:create_account("Kiko", "pass", "Company"),
  loginManager:create_account("Parfois", "pass", "Company"),
  loginManager:create_account("Asus", "pass", "Company"),
  {ok, ServerSocket} = gen_tcp:listen(11111, [binary, {packet, 4}, {reuseaddr, true}]),
  acceptor(ServerSocket).

acceptor(ServerSocket) ->
  {ok, Socket} = gen_tcp:accept(ServerSocket),
  io:fwrite("Connection accepted.\n"),
  gen_tcp:controlling_process(Socket, spawn(fun() -> authenticator:authenticate(Socket) end)),
  acceptor(ServerSocket).