package com.example.myapp3;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by steve on 01/03/16.
 */
public class GoalFragment extends Fragment implements ModelAware {

    public String homeAway = "Home";
    private ScoresheetModel model = null;
    private View view = null;
    private EditText periodField = null;
    private EditText clockField = null;
    private InputMethodManager imgr = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.goalfragment, container, false);
        TextView title = (TextView) view.findViewById(R.id.txtGoalScoredTitle);
        title.setText(homeAway + " Goal Scored");

        periodField = (EditText)view.findViewById(R.id.fldPeriod);
        clockField = (EditText)view.findViewById(R.id.fldClock);
        EditText scorerField = (EditText)view.findViewById(R.id.fldScoredBy);
        EditText assist1Field = (EditText)view.findViewById(R.id.fldAssist1);
        EditText assist2Field = (EditText)view.findViewById(R.id.fldAssist2);

        imgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        Button clearButton = (Button)view.findViewById(R.id.btnDone);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameClock clock = new GameClock(model.getPeriod());

                GoalEvent event = new GoalEvent();
                event.setPeriod(Integer.parseInt(periodField.getText().toString()));
                event.setTeam(homeAway);
                event.setGameTime(clock.gameTimeFromClock(clockField.getText().toString()));
                event.setPlayer(scorerField.getText().toString());
                event.setAssist1(Integer.parseInt("0"+assist1Field.getText().toString()));
                event.setAssist2(Integer.parseInt("0"+assist2Field.getText().toString()));

                model.getEvents().add(event);

                imgr.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);

                ((MyActivity)getActivity()).onModelUpdated(null);
                ((MyActivity)getActivity()).showHistory();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        clockField.requestFocus();
        periodField.setText(Integer.toString(model.getPeriod()));
        imgr.showSoftInput(clockField, InputMethodManager.SHOW_IMPLICIT);
    }

    @Override
    public void setModel(ScoresheetModel model) {
        this.model = model;
    }

    @Override
    public void onModelUpdated(ModelUpdate update) {

    }
}