package dimanchik.worktest;


import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class MainActivity extends AppCompatActivity {
    FloatingActionButton floatingActionButton;
    Button btnBground,btnAdd;
    ConstraintLayout constraintLayout;
    EditText editText;
    TextView textView;
    ProgressBar progressBar;
    BufferedReader in;
    String s;
    int a;
    AlertDialog alertDialog;
    private List<ImageSearch> images;
    private RecyclerView rv;
    Bitmap bmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        floatingActionButton =(FloatingActionButton)findViewById(R.id.floatingActionButton);

        //Получаем вид с файла dialog.xml
        LayoutInflater li = LayoutInflater.from(this);
        View dialogView = li.inflate(R.layout.dialog, null);

        //Создаем билдер для AlertDialog

        final AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(this);

        //Настраиваем dialog.xml для нашего AlertDialog:
        mDialogBuilder.setView(dialogView);

        //Настраиваем отображение поля для ввода текста в открытом диалоге:
        constraintLayout = (ConstraintLayout)dialogView.findViewById(R.id.constraintLayout);
        textView = (TextView) dialogView.findViewById(R.id.textView);
        editText = (EditText) dialogView.findViewById(R.id.editText);
        progressBar = (ProgressBar) dialogView.findViewById(R.id.progressBar);
        // Вешаем слушателя на кнопку добавления
        btnAdd = (Button) dialogView.findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                images.add(new ImageSearch(s,  bmap));
                rv.getAdapter().notifyDataSetChanged();
            }
        });
        // Вешаем слушателя на кнопку замены фона
        btnBground = (Button) dialogView.findViewById(R.id.btnBground);
        btnBground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                s = editText.getText().toString();
                Random rand = new Random();
                a = rand.nextInt(14)+1;
                MyTask mt = new MyTask();
                mt.execute();
            }
        });

        mDialogBuilder.setTitle("Картинки");
        alertDialog = mDialogBuilder.create();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.show();
            }
        });


        rv=(RecyclerView)findViewById(R.id.rv);
        GridLayoutManager glm = new GridLayoutManager(this,2);
        rv.setLayoutManager(glm);
        initializeData();
        initializeAdapter();


    }

    private void initializeData(){
        images = new ArrayList<>();
    }

    private void initializeAdapter() {
        RvAdapter adapter = new RvAdapter(images);
        rv.setAdapter(adapter);
    }


    class MyTask extends AsyncTask<Void, Void, Void> {

        String img;//Тут храним значение ссылки на картинку

        @Override
        protected Void doInBackground(Void... params) {
            Document doc = null;//Здесь хранится разобранный html документ
            try {
                //Считываем страницу
                doc = Jsoup.connect("https://yandex.ru/images/search?text=" + s).get();
            } catch (IOException e) {
                //Если не получилось считать
                e.printStackTrace();
            }
            //Если всё считалось, что вытаскиваем из html документа ссылку
            if (doc != null) {
                Elements metaElements = doc.select("img").eq(a);
                String href = metaElements.attr("src");
                img = href;
                Log.d("Log","doc = " + metaElements + "src=" + href);
            } else {
                img = "Ошибка";
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Загружаем каринку в с помощью библиотеки Picasso
            Picasso.get()
                    .load("http:" + img)
                    .error( R.drawable.err)
                    .into(new Target() {
                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            bmap = bitmap;
                            constraintLayout.setBackground(new BitmapDrawable(bitmap));
                            progressBar.setVisibility(View.GONE);
                        }
                        @Override
                        public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                        }
                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    });
            textView.setText(img);

        }
    }

}





















