package com.ahmadmsff.sekolahku;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ahmadmsff.sekolahku.APIHelper.APIServices;
import com.ahmadmsff.sekolahku.APIHelper.APIUtils;
import com.ahmadmsff.sekolahku.Model.ListStudent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddActivity extends AppCompatActivity {

    private ImageView image_siswa;
    private EditText nama, email, tanggal_lahir, alamat, nomor_handphone;
    private RadioGroup radioGroup;
    private RadioButton radioMale, radioFemale;
    private Button btnAdd;
    private APIServices apiServices;
    private Context context;
    private RelativeLayout progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        context = this;

        init();
    }

    private void init() {
        image_siswa = (ImageView) findViewById(R.id.add_image_siswa);
        nama = (EditText) findViewById(R.id.add_nama);
        email = (EditText) findViewById(R.id.add_email);
        tanggal_lahir = (EditText) findViewById(R.id.add_tanggal_lahir);
        radioGroup = (RadioGroup) findViewById(R.id.radio_jenis_kelamin);
        alamat = (EditText) findViewById(R.id.add_alamat);
        radioMale = (RadioButton) findViewById(R.id.radio_male);
        radioFemale = (RadioButton) findViewById(R.id.radio_female);
        nomor_handphone = (EditText) findViewById(R.id.add_nomor_handphone);
        btnAdd = (Button) findViewById(R.id.btn_add);
        progressBar = (RelativeLayout) findViewById(R.id.addProgressBar);

        final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Jakarta"));

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String format = "yyyy-MM-dd";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.US);
                tanggal_lahir.setText(simpleDateFormat.format(calendar.getTime()));
            }
        };

        tanggal_lahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(context, dateSetListener, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpan();
            }
        });
    }

    private void simpan() {
        String name = nama.getText().toString();
        String mail = email.getText().toString();
        int jenkel = radioGroup.getCheckedRadioButtonId();
        String bday = tanggal_lahir.getText().toString();
        String dest = alamat.getText().toString();
        String no = nomor_handphone.getText().toString();

        RadioButton radioGender = (RadioButton) findViewById(jenkel);
        String gender;
        if (radioGender.getText().equals("Laki-Laki")) {
            gender = "0";
        } else {
            gender = "1";
        }

        if (name.equals("")) {
            nama.requestFocus();
            Toast.makeText(context, "Nama tidak boleh kosong", Toast.LENGTH_SHORT).show();
        } else if (mail.equals("")) {
            email.requestFocus();
            Toast.makeText(context, "Email tidak boleh kosong", Toast.LENGTH_SHORT).show();
        } else if (bday.equals("")) {
            tanggal_lahir.requestFocus();
            Toast.makeText(context, "Tanggal Lahir tidak boleh kosong", Toast.LENGTH_SHORT).show();
        } else if (dest.equals("")) {
            alamat.requestFocus();
            Toast.makeText(context, "Alamat tidak boleh kosong", Toast.LENGTH_SHORT).show();
        } else if (no.equals("")) {
            nomor_handphone.requestFocus();
            Toast.makeText(context, "Nomor Handphone tidak boleh kosong", Toast.LENGTH_SHORT).show();
        } else {
            btnAdd.setEnabled(false);
            progressBar.bringToFront();
            progressBar.setVisibility(View.VISIBLE);
            apiServices = APIUtils.getAPIServices();
            apiServices.setSiswa(name, mail, gender, bday, dest, no).enqueue(new Callback<ListStudent>() {
                @Override
                public void onResponse(Call<ListStudent> call, Response<ListStudent> response) {
                    if (response.isSuccessful()) {
                        Boolean error = response.body().isError();
                        if(!error) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(context, "Data " + name + " berhasil disimpan", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            btnAdd.setEnabled(true);
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(context, response.body().getMsg().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ListStudent> call, Throwable t) {
                    btnAdd.setEnabled(true);
                }
            });
        }

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
