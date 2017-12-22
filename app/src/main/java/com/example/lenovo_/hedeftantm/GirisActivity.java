package com.example.lenovo_.hedeftantm;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.lenovo_.hedeftantm.Helper.DBHelper;
import com.example.lenovo_.hedeftantm.Helper.ReadURL;

public class GirisActivity extends AppCompatActivity {
    Button giris;
    DBHelper dbHelper;
    EditText kullaniciAdi,sifre;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris);
        giris=(Button)findViewById(R.id.button3);
        kullaniciAdi=(EditText)findViewById(R.id.editText);
        sifre=(EditText)findViewById(R.id.editText2);
        dbHelper=new DBHelper(getApplicationContext());
        UserObject uo=dbHelper.getUser();
        if (uo.getKullaniciAdi().length()>0){
            Intent intent = new Intent(GirisActivity.this, MainActivity.class);
            startActivity(intent);
        }
        giris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(GirisActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private class getData extends AsyncTask<String, String, String> {
        BlankFragment progress;

        @Override
        protected void onPreExecute() {

            FragmentManager fm =getSupportFragmentManager();
            progress = new BlankFragment();
            progress.show(fm, "");
        }

        @Override
        protected String doInBackground(String... values)
        {

            ReadURL readURL=new ReadURL();
            try{
                String data="";


                return "HATA";
            }
            catch (Exception e){
                return "HATA";
            }

        }

        @Override
        protected void onPostExecute(String results)
        {
            if (!results.equals("HATA")){
                dbHelper.deleteUser();
                dbHelper.insertUser(kullaniciAdi.getText().toString(),sifre.getText().toString(),"",results);
                Intent intent = new Intent(GirisActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }

            progress.dismiss();


        }
    }
}
