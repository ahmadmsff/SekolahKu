package com.ahmadmsff.sekolahku;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
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
import android.widget.Toolbar;

import com.ahmadmsff.sekolahku.APIHelper.APIServices;
import com.ahmadmsff.sekolahku.APIHelper.APIUtils;
import com.ahmadmsff.sekolahku.Adapter.StudentAdapter;
import com.ahmadmsff.sekolahku.Model.ListStudent;
import com.ahmadmsff.sekolahku.Model.Student;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditActivity extends AppCompatActivity {

    private ImageView image_siswa;
    private EditText id, nama, email, tanggal_lahir, alamat, nomor_handphone;
    private RadioGroup radioGroup;
    private RadioButton radioMale, radioFemale;
    private Button btnEdit, btnDelete;
    private APIServices apiServices;
    private RelativeLayout progressBar;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        context = this;

        init();
    }

    private void init() {
        progressBar = (RelativeLayout) findViewById(R.id.editProgressBar);
        image_siswa = (ImageView) findViewById(R.id.edit_image_siswa);
        id = (EditText) findViewById(R.id.edit_id);
        nama = (EditText) findViewById(R.id.edit_nama);
        email = (EditText) findViewById(R.id.edit_email);
        tanggal_lahir = (EditText) findViewById(R.id.edit_tanggal_lahir);
        radioGroup = (RadioGroup) findViewById(R.id.radio_jenis_kelamin);
        alamat = (EditText) findViewById(R.id.edit_alamat);
        radioMale = (RadioButton) findViewById(R.id.radio_male);
        radioFemale = (RadioButton) findViewById(R.id.radio_female);
        nomor_handphone = (EditText) findViewById(R.id.edit_nomor_handphone);
        btnEdit = (Button) findViewById(R.id.btn_simpan);
        btnDelete = (Button) findViewById(R.id.btn_hapus);

        getData();

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

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpan();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertbox = new AlertDialog.Builder(v.getRootView().getContext());
                alertbox.setMessage("Yakin ingin menghapus data " + nama.getText().toString() + "?");
                alertbox.setTitle("Confirmation");
                alertbox.setIcon(R.drawable.ic_warning);

                alertbox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        btnEdit.setEnabled(false);
                        btnDelete.setEnabled(false);
                        progressBar.setVisibility(View.VISIBLE);
                        APIServices apiServices = APIUtils.getAPIServices();
                        apiServices.deleteSiswa(Integer.parseInt(id.getText().toString())).enqueue(new Callback<ListStudent>() {
                            @Override
                            public void onResponse(Call<ListStudent> call, Response<ListStudent> response) {
                                btnEdit.setEnabled(true);
                                btnDelete.setEnabled(true);
                                progressBar.setVisibility(View.GONE);
                                if (response.isSuccessful()) {
                                    Boolean error = response.body().isError();
                                    if (!error) {
                                        Toast.makeText(context, "Berhasil menghapus data " + nama.getText().toString(), Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(context, MainActivity.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ListStudent> call, Throwable t) {
                                btnEdit.setEnabled(true);
                                btnDelete.setEnabled(true);
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(context, t.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                alertbox.setNegativeButton("No", null).show();
            }
        });

    }

    private void getData() {
        btnEdit.setEnabled(false);
        btnEdit.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
        apiServices = APIUtils.getAPIServices();
        Bundle extra = getIntent().getExtras();

        apiServices.getSiswaByID(extra.getString("id")).enqueue(new Callback<ListStudent>() {
            @Override
            public void onResponse(Call<ListStudent> call, Response<ListStudent> response) {
                btnEdit.setEnabled(true);
                btnDelete.setEnabled(true);
                progressBar.setVisibility(View.GONE);
                Boolean error = response.body().isError();
                if (!error) {
                    List<Student> students = response.body().getStudent();
                    if (students.get(0).getImage().equals("")){
                        Glide.with(context)
                                .load(R.drawable.me)
                                .centerCrop()
                                .into(image_siswa);
                    } else {
                        Glide.with(context)
                                .load("https://ahmadmsff.000webhostapp.com/api/smk/dist/images/siswa/" + students.get(0).getImage())
                                .centerCrop()
                                .into(image_siswa);
                    }
                    id.setText(students.get(0).getId().toString());
                    nama.setText(students.get(0).getNama().toString());
                    email.setText(students.get(0).getEmail().toString());
                    if(students.get(0).getJenis_kelamin().toString().equals("0")) {
                        radioMale.setChecked(true);
                    } else {
                        radioFemale.setChecked(true);
                    }
                    tanggal_lahir.setText(students.get(0).getTanggal_lahir().toString());
                    alamat.setText(students.get(0).getAlamat().toString());
                    nomor_handphone.setText(students.get(0).getNomor_handphone().toString());
                } else {
                    Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ListStudent> call, Throwable t) {
                btnEdit.setEnabled(true);
                btnDelete.setEnabled(true);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Oops!, Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void simpan() {
        Integer aidi = Integer.parseInt(id.getText().toString());
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
            btnEdit.setEnabled(false);
            btnDelete.setEnabled(false);
            progressBar.setVisibility(View.VISIBLE);
            apiServices = APIUtils.getAPIServices();
            apiServices.editSiswa(aidi, name, mail, gender, bday, dest, no).enqueue(new Callback<ListStudent>() {
                @Override
                public void onResponse(Call<ListStudent> call, Response<ListStudent> response) {
                    btnEdit.setEnabled(true);
                    btnDelete.setEnabled(true);
                    progressBar.setVisibility(View.GONE);
                    if (response.isSuccessful()) {
                        Boolean error = response.body().isError();
                        if(!error) {
                            Toast.makeText(context, "Data " + name + " berhasil diubah", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(context, response.body().getMsg().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ListStudent> call, Throwable t) {
                    btnEdit.setEnabled(true);
                    btnDelete.setEnabled(true);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(context, t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
