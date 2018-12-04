-module(server).

%% API
-export([main/0]).

%% Implementation
main() ->
  {ok, ServerSocket} = gen_tcp:listen(11111, [binary, {packet, line}, {reuseaddr, true}]),
  acceptor(ServerSocket).

acceptor(ServerSocket) ->
  {ok, Socket} = gen_tcp:accept(ServerSocket),
  io:fwrite('Connection accepted.'),
  gen_tcp:controlling_process(Socket, spawn(fun() -> temp(Socket) end)),
  acceptor(ServerSocket).

temp(Socket) ->
  temp(Socket).