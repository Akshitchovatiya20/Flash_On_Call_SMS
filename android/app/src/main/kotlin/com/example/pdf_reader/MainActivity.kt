package com.example.pdf_reader

import android.annotation.TargetApi
import android.content.ContentValues.TAG
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.annotation.NonNull
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel


class MainActivity : FlutterActivity() {

    private val CHANNEL = "demo"

    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        MethodChannel(
                flutterEngine.dartExecutor.binaryMessenger,
                CHANNEL
        ).setMethodCallHandler { call, result ->

//            if (call.method == "getBatteryLevel") {
//                val batteryLevel = getBatteryLevel()
//
//                if (batteryLevel != -1) {
//                    result.success(batteryLevel)
//                } else {
//                    result.error("UNAVAILABLE", "Battery level not available.", null)
//                }
//            } else {
//                result.notImplemented()
//            }
            if (call.method == "GetPhotos") {
                var data = getPdfList(this);
                result.success(data)

            }

        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    fun fetchGalleryImages(context: Context): ArrayList<String> {
        val galleryImageUrls: ArrayList<String>
        val columns = arrayOf(
                MediaStore.Files.FileColumns.DISPLAY_NAME,
                MediaStore.Files.FileColumns.DATE_ADDED,
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.MIME_TYPE,
        ) //get all columns of type image
        ///====================================================================

//        val collection: Uri
//
//
//        val sortOrder = MediaStore.Files.FileColumns.DATE_ADDED + " DESC"
//        val selection = MediaStore.Files.FileColumns.MIME_TYPE + " = ?"
//        val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("pdf")
//        val selectionArgs = arrayOf(mimeType)
//        collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL)
//        } else {
//            MediaStore.Files.getContentUri("external")
//        }
//
        galleryImageUrls = ArrayList()
//
//
////        getContentResolver().query(collection, projection, selection, selectionArgs, sortOrder).use { cursor ->
////            assert(cursor != null)
//
//            if (cursor!!.moveToFirst()) {
//                val columnData: Int = cursor!!.getColumnIndex(MediaStore.Files.FileColumns.DATA)
//                val columnName: Int = cursor!!.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME)
//                do {
//                    galleryImageUrls.add(cursor!!.getString(columnData))
//                    Log.d(TAG, "getPdf: " + cursor!!.getString(columnData))
//                    //you can get your pdf files
//                } while (cursor!!.moveToNext())
//            }
////        }
//


        ///====================================================================
//
//        val orderBy = MediaStore.Files.FileColumns.DATE_TAKEN //order data by date
//        val imagecursor: Cursor? = context.contentResolver.query(
//                MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL),
//                columns,
//                null,
//                null,
//                "$orderBy DESC"
//        )
//        galleryImageUrls = ArrayList()
//        imagecursor?.moveToFirst()
//        while (!imagecursor!!.isAfterLast()) {
//            val dataColumnIndex: Int =
//                    imagecursor.getColumnIndex(MediaStore.Files.FileColumns.DATA) //get column index
//            galleryImageUrls.add(imagecursor.getString(dataColumnIndex)) //get Image from column index
//            imagecursor.moveToNext()
//        }
        return galleryImageUrls
    }

    protected fun getPdfList(context: Context): ArrayList<String> {
        val pdfList = ArrayList<String>()
        val collection: Uri
        val projection = arrayOf(
                MediaStore.Files.FileColumns.DISPLAY_NAME,
                MediaStore.Files.FileColumns.DATE_ADDED,
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.MIME_TYPE)
        val sortOrder = MediaStore.Files.FileColumns.DATE_ADDED + " DESC"
        val selection = MediaStore.Files.FileColumns.MIME_TYPE + " = ?"
        val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("pdf")
        val selectionArgs = arrayOf(mimeType)
        collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            MediaStore.Files.getContentUri("external")
        }
        context.contentResolver.query(collection, projection, selection, selectionArgs, sortOrder).use { cursor ->
            assert(cursor != null)
            if (cursor!!.moveToFirst()) {
                val columnData: Int = cursor!!.getColumnIndex(MediaStore.Files.FileColumns.DATA)
                val columnName: Int = cursor!!.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME)
                do {
                    pdfList.add(cursor!!.getString(columnData))
                    Log.d(TAG, "getPdf: " + cursor!!.getString(columnData))
                    //you can get your pdf files
                } while (cursor!!.moveToNext())
            }
        }
        return pdfList
    }

//    fun SearchStorage(context: Context): ArrayList<ImagesFolder?>? {
//        folders.clear()
//        var position = 0
//        val uri: Uri
//        val cursor: Cursor
//        val column_index_data: Int
//        val column_index_folder_name: Int
//        var absolutePathOfImage: String? = null
//        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//        val projection =
//                arrayOf(MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
//        val orderBy = MediaStore.Images.Media.DATE_TAKEN
//        cursor = context.contentResolver.query(uri, projection, null, null, "$orderBy DESC")!!
//        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
//        column_index_folder_name =
//                cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
//        while (cursor.moveToNext()) {
//            absolutePathOfImage = cursor.getString(column_index_data)
//            Log.e("Column", absolutePathOfImage)
//            Log.e("Folder", cursor.getString(column_index_folder_name))
//            for (i in 0 until folders.size()) {
//                if (folders.get(i).getAllFolderName()
//                                .equals(cursor.getString(column_index_folder_name))
//                ) {
//                    boolean_folder = true
//                    position = i
//                    break
//                } else {
//                    boolean_folder = false
//                }
//            }
//            if (boolean_folder) {
//                val al_path = ArrayList<String?>()
//                al_path.addAll(folders.get(position).getAllImagePaths())
//                al_path.add(absolutePathOfImage)
//                folders.get(position).setAllImagePaths(al_path)
//            } else {
//                val al_path = ArrayList<String?>()
//                al_path.add(absolutePathOfImage)
//                val obj_model = ImagesFolder()
//                obj_model.setAllFolderName(cursor.getString(column_index_folder_name))
//                obj_model.setAllImagePaths(al_path)
//                folders.add(obj_model)
//            }
//        }
//        for (i in 0 until folders.size()) {
//            Log.d("FOLDER", folders.get(i).getAllFolderName())
//            Log.d("FOLDER SIZE", java.lang.String.valueOf(folders.get(i).getAllImagePaths().size()))
//            for (j in 0 until folders.get(i).getAllImagePaths().size()) {
//                Log.d("FOLDER THUMBNAIL", folders.get(i).getAllImagePaths().get(j))
//            }
//        }
//        //obj_adapter = new Adapter_PhotosFolder(getApplicationContext(),al_images);
//        //gv_folder.setAdapter(obj_adapter);
//        return folders
//    }


//}
}
