/*  Copyright 2016 Steve Leach

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/
package org.steveleach.scoresheet.model;

import java.util.Map;
import java.util.TreeMap;

/**
 * A team taking part in an ice hockey game.
 *
 * @author Steve Leach
 */
public class Team {
    private String name = null;
    private Map<Integer,Player> players = new TreeMap<>();

    public Team(String name) {
        this.name = name;
    }

    public Team() {
        this("");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<Integer, Player> getPlayers() {
        return players;
    }

    public void addPlayer(int number, String name) {
        getPlayers().put(number, new Player(number,name));
    }

    public String playerName(int number) {
        return getPlayers().containsKey(number) ? getPlayers().get(number).getName() : null;
    }

    /**
     * If there is an active player with the specified number, returns their name, otherwise returns an empty string.
     */
    public String activePlayerName(int number) {
        Player player = getPlayers().get(number);
        if (player != null) {
            if (player.isPlaying()) {
                return player.getName();
            }
        }
        return "";
    }
}
