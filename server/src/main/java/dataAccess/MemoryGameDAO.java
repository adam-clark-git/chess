package dataAccess;

import model.GameData;
import server.exceptions.DataAccessException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoryGameDAO implements GameDAO {
    private final Map<Integer, GameData> games = new HashMap<>();
    @Override
    public void clear() throws DataAccessException {
        games.clear();
    }
    @Override
    public void createGame(GameData game) throws DataAccessException {
        games.put(game.gameID(), game);
    }
    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return games.get(gameID);
    }
    @Override
    public List<GameData> listGames() throws DataAccessException {
        return new ArrayList<>(games.values());
    }
    @Override
    public void updateGame(GameData game) throws DataAccessException {
        games.put(game.gameID(), game);
    }
}
