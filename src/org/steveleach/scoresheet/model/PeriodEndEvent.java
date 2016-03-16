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

/**
 * Records the end of a period in an ice hockey game.
 *
 * Created by steve on 05/03/16.
 */
public class PeriodEndEvent extends GameEvent {

    public PeriodEndEvent() {
        setTeam("");
        setEventType("Period end");
        setPlayer("");
    }

    public PeriodEndEvent(int period, GameRules rules) {
        this();
        setPeriod(period);
        super.setClockTime("0000", rules);
    }

    @Override
    public String toString() {
        return String.format("%s - Period %d ended", gameTime, period);
    }
}
