package dresta.putra.aset.berkas;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import dresta.putra.aset.R;
import dresta.putra.aset.peta.DetailPetaActivity;
import pub.devrel.easypermissions.EasyPermissions;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission_group.STORAGE;

public class PaginationAdapterBerkas extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    private static final int ITEM = 0;
    private static final int LOADING = 1;
    ProgressDialog mProgressDialog;

    private List<BerkasPojo> BerkasPojos;
    private Context context;
    private String pathDownloaded = "";
    private static final int REQUEST_CODE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    private boolean isLoadingAdded = false;

    public PaginationAdapterBerkas(Context context) {
        this.context = context;
        BerkasPojos = new ArrayList<>();
    }


    public void setBerkasPojos(List<BerkasPojo> BerkasPojos) {
        this.BerkasPojos = BerkasPojos;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }
        return viewHolder;
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, @NonNull LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.adapter_berkas, parent, false);
        viewHolder = new BerkasPojoVH(v1);
        return viewHolder;
    }



    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        final BerkasPojo result = BerkasPojos.get(position); // BerkasPojo
        switch (getItemViewType(position)) {
            case ITEM:
                final BerkasPojoVH Vh = (BerkasPojoVH) holder;

// instantiate it within the onCreate method
                mProgressDialog = new ProgressDialog(context);
                mProgressDialog.setMessage("Mendownload "+result.getNama_berkas());
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                mProgressDialog.setCancelable(true);

                Vh.TxvNamaBerkas.setText(result.getNama_berkas());
                Vh.TxvDeskripsiBerkas.setText(result.getDeskripsi_berkas());


                Vh.IBvDownload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Activity activity = (Activity) context;


                        mProgressDialog.show();
                        final DownloadTask downloadTask = new DownloadTask(context);
                        downloadTask.execute(result.getBerkas());
                        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                downloadTask.cancel(true); //cancel the task
                                mProgressDialog.dismiss();
                            }
                        });


// execute this when the downloader must be fired

                    }
                });
                break;
            case LOADING:
//                Do nothing
                break;
        }

    }



    @Override
    public int getItemCount() {
        return BerkasPojos == null ? 0 : BerkasPojos.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == BerkasPojos.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }


    /*
   Helpers
   _________________________________________________________________________________________________
    */

    public void add(BerkasPojo r) {
        BerkasPojos.add(r);
        notifyItemInserted(BerkasPojos.size() - 1);
    }

    public void addAll(@NonNull List<BerkasPojo> moveResults) {
        for (BerkasPojo result : moveResults) {
            add(result);
        }
    }

    public void remove(BerkasPojo r) {
        int position = BerkasPojos.indexOf(r);
        if (position > -1) {
            BerkasPojos.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new BerkasPojo());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = BerkasPojos.size() - 1;
        BerkasPojo result = getItem(position);

        if (result != null) {
            BerkasPojos.remove(position);
            notifyItemRemoved(position);
        }
    }

    public BerkasPojo getItem(int position) {
        return BerkasPojos.get(position);
    }



   /*
   View Holders
   _________________________________________________________________________________________________
    */

    /**
     * Main list's content ViewHolder
     */
    protected class BerkasPojoVH extends RecyclerView.ViewHolder {
        private TextView TxvNamaBerkas,TxvDeskripsiBerkas;
        private ImageButton IBvDownload;
        public BerkasPojoVH(@NonNull View itemView) {
            super(itemView);
            TxvNamaBerkas =  itemView.findViewById(R.id.TxvNamaBerkas);
            TxvDeskripsiBerkas =  itemView.findViewById(R.id.TxvDeskripsiBerkas);
            IBvDownload =  itemView.findViewById(R.id.IBvDownload);
        }
    }


    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(@NonNull View itemView) {
            super(itemView);
        }
    }
    public class DownloadTask extends AsyncTask<String, Integer, String> {

        private Context context;
        private PowerManager.WakeLock mWakeLock;

        public DownloadTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... sUrl) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }

                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();
                String fileName = getFileNameFromURL(sUrl[0]);
                // download the file
                String dirPath = createDirectory();
                input = connection.getInputStream();
                output = new FileOutputStream(dirPath + File.separator +fileName);

                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }

                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // take CPU lock to prevent CPU from going off if the user
            // presses the power button during download
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            mWakeLock.acquire();
            mProgressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // if we get here, length is known, now set indeterminate to false
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMax(100);
            mProgressDialog.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            mWakeLock.release();
            mProgressDialog.dismiss();
            if (result != null){
                Toast.makeText(context,"Download Gagal ", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(context,"File berhasil terdownload."+((pathDownloaded != null) ? " Cek di "+pathDownloaded : ""), Toast.LENGTH_SHORT).show();
            }
        }
        public String getFileNameFromURL(String url) {
            if (url == null) {
                return "";
            }
            try {
                URL resource = new URL(url);
                String host = resource.getHost();
                if (host.length() > 0 && url.endsWith(host)) {
                    // handle ...example.com
                    return "";
                }
            }
            catch(MalformedURLException e) {
                return "";
            }

            int startIndex = url.lastIndexOf('/') + 1;
            int length = url.length();

            // find end index for ?
            int lastQMPos = url.lastIndexOf('?');
            if (lastQMPos == -1) {
                lastQMPos = length;
            }

            // find end index for #
            int lastHashPos = url.lastIndexOf('#');
            if (lastHashPos == -1) {
                lastHashPos = length;
            }

            // calculate the end index
            int endIndex = Math.min(lastQMPos, lastHashPos);
            return url.substring(startIndex, endIndex);
        }
        private String createDirectory(){
            String path = null;
            if (Environment.getExternalStorageState() == null) {
                //create new file directory object
                File directory = new File(Environment.getDataDirectory() + "/kebumenkab/");
                File photoDirectory = new File(Environment.getDataDirectory() + "/kebumenkab/");
                /*
                 * this checks to see if there are any previous test photo files
                 * if there are any photos, they are deleted for the sake of
                 * memory
                 */
                if (photoDirectory.exists()) {
                    File[] dirFiles = photoDirectory.listFiles();
                    if (dirFiles.length != 0) {
                        for (int ii = 0; ii <= dirFiles.length; ii++) {
                            dirFiles[ii].delete();
                        }
                    }
                }
                // if no directory exists, create new directory
                if (!directory.exists()) {
                    directory.mkdir();
                }
                path = directory.getPath();

                // if phone DOES have sd card
            } else if (Environment.getExternalStorageState() != null) {
                // search for directory on SD card
                File directory = new File(Environment.getExternalStorageDirectory() + "/kebumenkab/");
                File photoDirectory = new File(Environment.getExternalStorageDirectory() + "/kebumenkab/");
                if (photoDirectory.exists()) {
                    File[] dirFiles = photoDirectory.listFiles();
                    if (dirFiles.length > 0) {
                        for (int ii = 0; ii < dirFiles.length; ii++) {
                            dirFiles[ii].delete();
                        }
                        dirFiles = null;
                    }
                }
                // if no directory exists, create new directory to store test
                // results
                if (!directory.exists()) {
                    directory.mkdir();
                }
                path = directory.getPath();
            }// end of SD card checking
            pathDownloaded = path;
            return path;
        }
    }



}

