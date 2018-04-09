package br.com.felipedeveloper.gestaofinanceira.View;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import br.com.felipedeveloper.gestaofinanceira.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CarteiraActivity extends AppCompatActivity {
    DatePickerDialog.OnDateSetListener date;
    @BindView(R.id.textInpvalor)
    TextInputLayout layoutvalor;
    @BindView(R.id.texteditvalor)
    EditText textvalor;
    @BindView(R.id.textInpdata)
    TextInputLayout layoutdata;
    @BindView(R.id.texteditdata)
    EditText textdata;
    @BindView(R.id.textInputtitulo)
    TextInputLayout textInptitulo;
    @BindView(R.id.textedittitulo)
    EditText texttitulo;
    @BindView(R.id.btnconfirmar)
    Button btconfirmar;
    FirebaseDatabase database;
    DatabaseReference myreference;
    Calendar myCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carteiira);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("");
        ButterKnife.bind(this);
        configFirebase();
        myCalendar = Calendar.getInstance();
        textvalor.setCompoundDrawables(null, null, getResources().getDrawable(R.drawable.ic_add), null);
         final android.support.v7.widget.SwitchCompat aSwitch = (android.support.v7.widget.SwitchCompat) findViewById(R.id.switchaaddvalor);


        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    aSwitch.setText("Lançamento Credito");
                    aSwitch.setTextColor(getResources().getColor(R.color.colorSwitchcredito));
                }else {
                    aSwitch.setText("Lançamento Debito");
                    aSwitch.setTextColor(getResources().getColor(R.color.colorSwitchdebito));
                }
            }
        });
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };


        textdata.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    new DatePickerDialog(CarteiraActivity.this, date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }


            }
        });
        

    }

    private void updateLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        textdata.setText(sdf.format(myCalendar.getTime()));
    }

    private void configFirebase(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        myreference = firebaseDatabase.getReference().child("financeiro").child("carteira");

    }

}
