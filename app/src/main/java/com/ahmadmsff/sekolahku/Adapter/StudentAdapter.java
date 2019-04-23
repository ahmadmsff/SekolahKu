package com.ahmadmsff.sekolahku.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ahmadmsff.sekolahku.APIHelper.APIServices;
import com.ahmadmsff.sekolahku.APIHelper.APIUtils;
import com.ahmadmsff.sekolahku.EditActivity;
import com.ahmadmsff.sekolahku.MainActivity;
import com.ahmadmsff.sekolahku.Model.ListStudent;
import com.ahmadmsff.sekolahku.Model.Student;
import com.ahmadmsff.sekolahku.R;
import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentAdapterViewHolder> {
    private Context context;
    private List<Student> students;

    public StudentAdapter(Context context, List<Student> students) {
        this.context = context;
        this.students = students;
    }

    @NonNull
    @Override
    public StudentAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_siswa, viewGroup, false);
        return new StudentAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentAdapterViewHolder holder, int i) {
        Student student = students.get(i);
        String name = student.getNama();
        String email = student.getEmail();
        String jenis_kelamin = student.getJenis_kelamin();
        String image = student.getImage();

        holder.nama_siswa.setText(name);
        holder.email_siswa.setText(email);
        if (jenis_kelamin.equals("0")) {
            holder.image_siswa.setBorderColor(Color.parseColor("#0097e6"));
        } else {
            holder.image_siswa.setBorderColor(Color.parseColor("#e84393"));
        }

        if (image.equals("")){
            Glide.with(context)
                    .load(R.drawable.me)
                    .centerCrop()
                    .into(holder.image_siswa);
        } else {
            Glide.with(context)
                    .load("https://ahmadmsff.000webhostapp.com/api/smk/dist/images/siswa/" + image)
                    .centerCrop()
                    .into(holder.image_siswa);
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, EditActivity.class);
                intent.putExtra("id", student.getId().toString());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    public class StudentAdapterViewHolder extends RecyclerView.ViewHolder {

        private TextView nama_siswa, email_siswa;
        private CircleImageView image_siswa;
        private ProgressBar progressBar;
        private CardView cardView;

        public StudentAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            nama_siswa = (TextView) itemView.findViewById(R.id.nama_siswa);
            email_siswa = (TextView) itemView.findViewById(R.id.email_siswa);
            image_siswa = (CircleImageView) itemView.findViewById(R.id.image_siswa);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
        }
    }
}
