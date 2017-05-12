package zentcode02.parks.utils;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import zentcode02.parks.BuildConfig;
import zentcode02.parks.R;
import zentcode02.parks.dbModels.ItemCheck;
import zentcode02.parks.dbModels.Sincronizar;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageUpload extends Fragment implements View.OnClickListener {

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private int item_id;
    private ImageView imageViewUpload;
    private Button btnAgregarImagen, btnCambiarImagen;
    private ItemCheck itemCheck;
    private String pictureImagePath = "";
    private File imgFile;

    public ImageUpload() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //GET ARGS FRAGMENT
        item_id = getArguments().getInt("item_id");
        //Query tabla Items Check
        queryItemChecks(item_id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.image_upload_fragment, container, false);

        imageViewUpload = (ImageView) view.findViewById(R.id.imageViewUpload);
        btnAgregarImagen = (Button) view.findViewById(R.id.btnAgregarImagen);
        btnCambiarImagen = (Button) view.findViewById(R.id.btnCambiarImagen);

        if (!Objects.equals(itemCheck.getImagen(), "") && !itemCheck.getImagen().isEmpty()) {
            btnAgregarImagen.setVisibility(View.GONE);
//            imgFile = new  File(itemCheck.getImagen());
            Uri imageUri = Uri.parse(itemCheck.getImagen());
            Bitmap imageBitmap = BitmapFactory.decodeFile(imageUri.getPath());
            scaleImagePreview(imageBitmap);
//            Bitmap imgBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
//            imageViewUpload.setImageBitmap(imgBitmap);
        } else {
            btnCambiarImagen.setVisibility(View.GONE);
        }

        btnAgregarImagen.setOnClickListener(this);
        btnCambiarImagen.setOnClickListener(this);

        return view;
    }

    private void queryItemChecks(int item_id) {
        itemCheck = ItemCheck.findById(ItemCheck.class, item_id);
        Log.d("item query", itemCheck.getNombre());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAgregarImagen:
                StartCameraIntent();
                break;
            case R.id.btnCambiarImagen:
                StartCameraIntent();
                break;
        }
    }

    private void StartCameraIntent() {
        try {
            dispatchTakePictureIntent();
        } catch (IOException e) {
            Log.d("IO Exception", e.toString());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Log.d("image path", pictureImagePath);

            // Show the thumbnail on ImageView
            Uri imageUri = Uri.parse(pictureImagePath);
            File imgFile = new File(imageUri.getPath());
            if (imgFile.exists()) {
                System.out.println("Guardo img en la database");
                saveImageDatabase(pictureImagePath);
                Bitmap imageBitmap = BitmapFactory.decodeFile(imageUri.getPath());
                if (imageBitmap != null) {
                    scaleImagePreview(imageBitmap);
                }
            }

            // ScanFile so it will be appeared on Gallery
            MediaScannerConnection.scanFile(getActivity(),
                    new String[]{imageUri.getPath()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                        }
                    });
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Uri imageUri = Uri.parse(pictureImagePath);
            File imgFile = new File(imageUri.getPath());
            if (imgFile.exists()) {
                Log.d("entro en el delete ", String.valueOf(imgFile.exists()));
                imgFile.delete();
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";

        File mFile=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + "Parks" + File.separator);
        Log.d("is directory pictures", String.valueOf(mFile.isDirectory()));
        if (!mFile.isDirectory()) {
            mFile.mkdir();
        }

        /*File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");*/
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                mFile      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        pictureImagePath = "file:" + image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() throws IOException {
        Intent takePictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                return;
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
//                Uri photoURI = Uri.fromFile(createImageFile());
                Uri photoURI = FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".provider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private void saveImageDatabase(String imgPath) {
        itemCheck.setImagen(imgPath);
        itemCheck.save();
        Sincronizar sincronizar = new Sincronizar(
                "items_check",
                itemCheck.getId(),
                "update",
                false,
                1
        );
        sincronizar.save();
        redirectBack();
    }

    private void redirectBack() {
        new AlertDialog.Builder(getActivity())
                .setTitle("Imagen cargada con éxito!")
                .setMessage("La imagen ha sido añadida al formulario.")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.popBackStackImmediate();
                    }
                }).setCancelable(false).show();
    }

    private void scaleImagePreview(Bitmap imgBitmap) {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        Log.e("Screen width ", " "+width);
        Log.e("Screen height ", " "+height);
        Log.e("img width ", " "+imgBitmap.getWidth());
        Log.e("img height ", " "+imgBitmap.getHeight());
        float scaleHt =(float) width/imgBitmap.getWidth();
        Log.e("Scaled percent ", " "+scaleHt);
        Bitmap scaled = Bitmap.createScaledBitmap(imgBitmap, width, (int)(imgBitmap.getWidth()*scaleHt), true);
        imageViewUpload.setImageBitmap(scaled);
    }

}
