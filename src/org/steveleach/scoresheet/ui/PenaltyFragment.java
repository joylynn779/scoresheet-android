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
package org.steveleach.scoresheet.ui;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import org.steveleach.ihscoresheet.R;
import org.steveleach.scoresheet.model.*;

/**
 * Implementation code for the new Penalty UI fragment.
 *
 * @author Steve Leach
 */
public class PenaltyFragment extends ScoresheetFragment {

    private View view;
    private EditText periodField;
    private EditText clockField;
    private AutoCompleteTextView penaltyField;
    private EditText playerField;
    private InputMethodManager imgr;
    private EditText minutesField;
    private PenaltyEvent eventToEdit = null;
    private EditText plusMinsField;

    public void setTeam(String homeAway) {
        this.team = homeAway;
    }

    private String team = "Home";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.penaltyfragment, container, false);

        TextView title = (TextView)view.findViewById(R.id.txtPenaltyTitle);
        title.setText(getString(R.string.penaltyTitle, team));

        periodField = (EditText)view.findViewById(R.id.fldPenaltyPeriod);
        clockField = (EditText)view.findViewById(R.id.fldPenaltyClock);
        penaltyField = (AutoCompleteTextView)view.findViewById(R.id.fldPenaltyCode);
        playerField = (EditText)view.findViewById(R.id.fldPenaltyPlayer);
        minutesField = (EditText)view.findViewById(R.id.fldPenaltyMins);
        plusMinsField = (EditText)view.findViewById(R.id.fldPenaltyPlusMins);

        clockField.setFilters( new InputFilter[]{ new InputFilter.LengthFilter(4) });
        periodField.setFilters( new InputFilter[]{ new InputFilter.LengthFilter(1) });
        penaltyField.setFilters( new InputFilter[]{ new InputFilter.LengthFilter(5) });
        minutesField.setFilters( new InputFilter[]{ new InputFilter.LengthFilter(2) });
        plusMinsField.setFilters( new InputFilter[]{ new InputFilter.LengthFilter(2) });
        playerField.setFilters( new InputFilter[]{ new InputFilter.LengthFilter(3) });

        Team teamData = model.getTeam(team);

        ScoresheetFocusChangeListener.setClockField(clockField,periodField,model);
        ScoresheetFocusChangeListener.setPlayerNumField(playerField,view.findViewById(R.id.txtPenaltyPlayer),teamData);
        periodField.setOnFocusChangeListener(new ScoresheetFocusChangeListener());
        minutesField.setOnFocusChangeListener(new ScoresheetFocusChangeListener());
        penaltyField.setOnFocusChangeListener(new ScoresheetFocusChangeListener());

        String[] penaltyCodes = getActivity().getResources().getStringArray(R.array.penaltyCodes);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_dropdown_item_1line,penaltyCodes);
        penaltyField.setAdapter(adapter);
        penaltyField.setThreshold(1);

        imgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        view.findViewById(R.id.btnPenaltyDone).setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    penaltyDone(view);
                }
            }
        );
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (view != null) {
            if (eventToEdit != null) {
                periodField.setText(Integer.toString(eventToEdit.getPeriod()));
                clockField.setText(eventToEdit.getClockTime());
                playerField.setText(Integer.toString(eventToEdit.getPlayer()));
                penaltyField.setText(eventToEdit.getSubType());
                minutesField.setText(Integer.toString(eventToEdit.getMinutes()));
                if (eventToEdit.getPlusMins() > 0) {
                    plusMinsField.setText(Integer.toString(eventToEdit.getPlusMins()));
                }
            } else {
                periodField.setText(Integer.toString(model.getPeriod()));
            }
            clockField.requestFocus();
            imgr.showSoftInput(clockField, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public void penaltyDone(View view) {
        try {
            PenaltyEvent event = (eventToEdit == null) ? new PenaltyEvent() : eventToEdit;
            event.setPeriod(Integer.parseInt("0"+periodField.getText().toString()));
            event.setClockTime(clockField.getText().toString());
            event.setTeam(team);
            event.setPlayer(Integer.parseInt("0"+playerField.getText().toString()));
            event.setSubType(penaltyField.getText().toString());
            event.setMinutes(Integer.parseInt("0"+minutesField.getText().toString()));
            event.setPlusMins(Integer.parseInt("0"+plusMinsField.getText().toString()));
            if (eventToEdit == null) {
                model.addEvent(event);
            }
            model.setChanged(true);
        } catch (IllegalArgumentException e) {
            Toast.makeText(getActivity().getApplicationContext(), "Error, not created", Toast.LENGTH_LONG);
        }

        imgr.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);

        ((DefaultFragmentActivity)getActivity()).showDefaultFragment();
    }

    public void setEventToEdit(PenaltyEvent event) {
        this.eventToEdit = event;
    }
}
