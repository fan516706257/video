package hfad.com.video;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

public class MainActivity extends Activity {

    private SurfaceView sfview;
    private SurfaceHolder holder;
    private SeekBar sb;
    private MediaPlayer mp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        sb = (SeekBar)findViewById(R.id.sb);
        sfview = (SurfaceView)findViewById(R.id.sfView);
        holder = sfview.getHolder();


        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar,int progress,boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (mp !=null)
                    mp.seekTo(seekBar.getProgress());
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    public void doPlay(View view){
        if(mp !=null)
            return;
        mp=new MediaPlayer();


        @SuppressLint("SdCardPath")
        String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Movies/1.mp4";
        try{
            mp.setDataSource(path);
            mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mp.setDisplay(holder);
            mp.prepare();
            sb.setMax(mp.getDuration());
            mp.start();



            new Thread(new Runnable() {
                @Override
                public void run() {


                    while (mp.getCurrentPosition()<=sb.getMax()){

                        sb.setProgress(mp.getCurrentPosition());
                        SystemClock.sleep(200);
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void doPause(View view){
        if(mp!=null&&mp.isPlaying()){
            mp.pause();//暂停
            ((Button)view).setText("继续播放");
        }else if(mp!=null&&mp.isPlaying()==false){
            mp.start();//播放
            ((Button)view).setText("暂停");
        }

    }
    public void doStop(View view){
        if(mp!=null){
            sb.setProgress(0);
            mp.stop();
            mp.release();
            mp=null;
        }

    }

}
