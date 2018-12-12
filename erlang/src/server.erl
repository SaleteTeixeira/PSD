-module(server).

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
      case messages:decode_msg(Bin, 'LoginRequest') of
        {'LoginRequest', Username, Password, Role} ->
          io:fwrite('Login request. ~p ~p ~p\n', [Username, Password, Role]),
          case loginManager:login(Username, Password, Role) of
            ok ->
              io:fwrite('Login ok.\n'),
              Reply = messages:encode_msg(#'Reply'{result = true, message = "Login ok"}),
              gen_tcp:send(Socket, Reply),
              handle(Socket, Username);
            _ ->
              io:fwrite('Login not ok.\n'),
              Reply = messages:encode_msg(#'Reply'{result = false, message = "Login not ok"}),
              gen_tcp:send(Socket, Reply),
              authenticate(Socket)
          end;
        _ ->
          io:fwrite('WRONG.\n'),
          Reply = messages:encode_msg(#'Reply'{result = false, message = "WRONG LOGIN"}),
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
      io:fwrite('~p~n', [messages:decode_msg(Bin, 'Request')]),
      case messages:decode_msg(Bin, 'Request') of
        {'Request', "LogoutRequest"} ->
          logout(Socket, Username, Bin);
        {'Request', "AuctionBid"} ->
          investor_bid(Socket, Username, Bin);
        {'Request', "FixedSubscription"} ->
          investor_fixed(Socket, Username, Bin);
        _ ->
          io:fwrite('WRONG REQUEST.\n'),
          handle(Socket, Username)
      end;
    {tcp_closed, _} ->
      io:fwrite('User terminated in handle.~n'),
      loginManager:logout(Username),
      invalid;
    {tcp_error, _, _} ->
      io:fwrite('TCP error in handle.~n'),
      loginManager:logout(Username),
      invalid
  end.

investor_fixed(Socket, Username, Bin) ->
  io:fwrite('Fixed subscription request. ~p~n', [Username]),
  case messages:decode_msg(Bin, 'FixedSubscription') of
    _ ->
      io:fwrite('Fixed subscription request not ok.\n'),
      Reply = messages:encode_msg(#'Reply'{result = false, message = "Malformed fixed subscription request"}),
      gen_tcp:send(Socket, Reply),
      handle(Socket, Username)
  end.

investor_bid(Socket, Username, Bin) ->
  io:fwrite('Auction bid request. ~p~n', [Username]),
  case messages:decode_msg(Bin, 'AuctionBid') of
    _ ->
      io:fwrite('Auction bid request not ok.\n'),
      Reply = messages:encode_msg(#'Reply'{result = false, message = "Malformed auction bid request"}),
      gen_tcp:send(Socket, Reply),
      handle(Socket, Username)
  end.


logout(Socket, Username, Bin) ->
  io:fwrite('Logout request. ~p~n', [Username]),
  case messages:decode_msg(Bin, 'LogoutRequest') of
    {_, _, Username} ->
      case loginManager:logout(Username) of
        ok ->
          io:fwrite('Logout ok.\n'),
          Reply = messages:encode_msg(#'Reply'{result = true, message = "Logout ok"}),
          gen_tcp:send(Socket, Reply),
          authenticate(Socket);
        _ ->
          io:fwrite('Logout not ok.\n'),
          Reply = messages:encode_msg(#'Reply'{result = false, message = "Logout not ok"}),
          gen_tcp:send(Socket, Reply),
          handle(Socket, Username)
      end;
    _ ->
      io:fwrite('Logout not ok.\n'),
      Reply = messages:encode_msg(#'Reply'{result = false, message = "Logout not ok"}),
      gen_tcp:send(Socket, Reply),
      handle(Socket, Username)
  end.