package technologies.d.digital.whatsappdirect;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.rilixtech.Country;
import com.rilixtech.CountryCodePicker;



public class MainActivity extends AppCompatActivity {

    CountryCodePicker ccp;
    AppCompatEditText editphoneNumber;
    Button buttonclr;
    Button buttoncalllog;
    Button buttonsend;
    EditText edittext;
    String cc;
    String mynum10;
    String mynum;

    String mytxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        editphoneNumber = (AppCompatEditText) findViewById(R.id.phone_number_edit);
        buttonclr = (Button) findViewById(R.id.buttonclr);
        buttonclr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editphoneNumber.setText("");
            }
        });
        buttoncalllog = (Button) findViewById(R.id.buttoncalllog);
        edittext = (EditText)findViewById(R.id.edittext);
        buttonsend = (Button)findViewById(R.id.buttonsend);



        buttoncalllog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this , technologies.d.digital.whatsappdirect.CallLog.class);
                startActivity(intent);

            }
        });



        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected(Country country) {
                Toast.makeText(MainActivity.this , "updated  "+ country.getName(),Toast.LENGTH_SHORT).show();


            }
        });
        mynum10 = getIntent().getStringExtra("mynum10");
        editphoneNumber.setText(mynum10);



        buttonsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                mytxt = edittext.getText().toString();
                cc = ccp.getSelectedCountryCode();
                mynum = editphoneNumber.getText().toString();

                if(mynum.isEmpty()){
                    editphoneNumber.setError("Invaid Number");
                }else {
                    final String mynumber = cc + mynum;
                    final String url = "https://api.whatsapp.com/send?l=en&phone=" + mynumber + "&text=" + mytxt;
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }

            }
        });


    }


}
