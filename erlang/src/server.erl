-module(server).

%% API
-export([main/0]).

%% Import
-include("messages.hrl").

%% Implementation
number_of_exchanges() -> 3.

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
              handle(Socket, Username, Role);
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

handle(Socket, Username, Role) ->
  case Role of
    "Investor" ->
      handle_investor(Socket, Username);
    "Company" ->
      handle_company(Socket, Username);
    _ ->
      io:fwrite('Blimey\n')
  end.

hash_string(String) -> hash_string(String, 0).
hash_string([], Acc) -> Acc;
hash_string([H | T], Acc) ->
  hash_string(T, Acc + (H + ((1 - rand:uniform()) * 100))).

get_ip(Index) ->
  (Index rem number_of_exchanges()) + 12345.

get_exchange(Company) ->
  get_ip(hash_string(Company)).


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% COMPANY
handle_company(ClientSocket, Company) ->
  {ok, ExchangeSocket} = gen_tcp:connect("localhost", get_exchange(Company), [binary, {packet, 4}, {reuseaddr, true}]),
  handle_company(ClientSocket, Company, ExchangeSocket).
handle_company(ClientSocket, Company, ExchangeSocket) ->
  receive
    {tcp, _, Bin} ->
      io:fwrite('~p~n', [messages:decode_msg(Bin, 'Request')]),
      case messages:decode_msg(Bin, 'Request') of
        {'Request', "LogoutRequest"} ->
          logout_company(ClientSocket, Company, ExchangeSocket, Bin);
        {'Request', "Auction"} ->
          company_auction(ClientSocket, Company, ExchangeSocket, Bin);
        {'Request', "FixedLoan"} ->
          company_fixed(ClientSocket, Company, ExchangeSocket, Bin);
        _ ->
          io:fwrite('WRONG REQUEST.\n'),
          handle_company(ClientSocket, Company, ExchangeSocket)
      end;
    {tcp_closed, _} ->
      io:fwrite('Company terminated in handle.~n'),
      loginManager:logout(Company),
      invalid;
    {tcp_error, _, _} ->
      io:fwrite('TCP error in handle.~n'),
      loginManager:logout(Company),
      invalid
  end.

company_fixed(ClientSocket, Company, ExchangeSocket, Bin) ->
  io:fwrite('Fixed loan creation request. ~p~n', [Company]),
  io:fwrite('Forwarding'),
  gen_tcp:send(ExchangeSocket, Bin),
  receive
    {tcp, _, Bin} ->
      gen_tcp:send(ClientSocket, Bin),
      handle_company(ClientSocket, Company, ExchangeSocket);
    _ ->
      loginManager:logout(Company)
  end.

company_auction(ClientSocket, Company, ExchangeSocket, Bin) ->
  io:fwrite('Auction creation request. ~p~n', [Company]),
  io:fwrite('Forwarding'),
  gen_tcp:send(ExchangeSocket, Bin),
  receive
    {tcp, _, Bin} ->
      gen_tcp:send(ClientSocket, Bin),
      handle_company(ClientSocket, Company, ExchangeSocket);
    _ ->
      loginManager:logout(Company)
  end.

logout_company(Socket, Company, ExchangeSocket, Bin) ->
  io:fwrite('Company logout request. ~p~n', [Company]),
  case messages:decode_msg(Bin, 'LogoutRequest') of
    {_, _, Company} ->
      case loginManager:logout(Company) of
        ok ->
          io:fwrite('Logout ok.\n'),
          Reply = messages:encode_msg(#'Reply'{result = true, message = "Logout ok"}),
          gen_tcp:send(Socket, Reply),
          gen_tcp:close(ExchangeSocket),
          authenticate(Socket);
        _ ->
          io:fwrite('Logout not ok.\n'),
          Reply = messages:encode_msg(#'Reply'{result = false, message = "Logout not ok"}),
          gen_tcp:send(Socket, Reply),
          handle_company(Socket, Company, ExchangeSocket)
      end;
    _ ->
      io:fwrite('Logout not ok.\n'),
      Reply = messages:encode_msg(#'Reply'{result = false, message = "Logout not ok"}),
      gen_tcp:send(Socket, Reply),
      handle_company(Socket, Company, ExchangeSocket)
  end.


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% INVESTOR
get_exchange_map(ExchangeMap, Accum) ->
  N = number_of_exchanges() * 2,
  case Accum of
    N -> ExchangeMap;
    _ ->
      Port = get_ip(Accum),
      {ok, Socket} = gen_tcp:connect("localhost", Port, [binary, {packet, 4}, {reuseaddr, true}]),
      maps:put(Port, Socket, ExchangeMap),
      get_exchange_map(ExchangeMap, Accum + 1)
  end.
handle_investor(ClientSocket, Username) ->
  ExchangeMap = get_exchange_map(#{}, number_of_exchanges()),
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
  Port = get_exchange(Company),
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
  Port = get_exchange(Company),
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
          authenticate(ClientSocket);
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