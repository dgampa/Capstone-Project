package com.metauniversity.behindthebusiness;

import android.os.Environment;
import android.util.Log;


import org.mp4parser.BasicContainer;;
import org.mp4parser.muxer.Movie;
import org.mp4parser.muxer.Track;
import org.mp4parser.muxer.builder.DefaultMp4Builder;
import org.mp4parser.muxer.container.mp4.MovieCreator;
import org.mp4parser.muxer.tracks.AppendTrack;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class Mp4ParserWrapper {

    public static final String TAG = Mp4ParserWrapper.class.getSimpleName();

    public static final int FILE_BUFFER_SIZE = 1024;

    /**
     * Appends mp4 audio/video from {@code anotherFileName} to {@code mainFileName}.
     */
    public static boolean append(String mainFileName, String anotherFileName) {
        boolean rvalue = false;
        try {
            File targetFile = new File(mainFileName);
            File anotherFile = new File(anotherFileName);
            if (targetFile.exists() && targetFile.length()>0) {
                String tmpFileName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).getAbsolutePath() + File.separator + "tmp.mp4";
                //mainfile=vishal0
                //another file=vishal1
                //tmpfile=vishal0.tmp

                append(mainFileName, anotherFileName, tmpFileName);
                copyFile(tmpFileName, mainFileName);
                anotherFile.delete();
                new File(tmpFileName).delete();
                rvalue = true;
            } else if ( targetFile.createNewFile() ) {
                copyFile(anotherFileName, mainFileName);
                anotherFile.delete();
                rvalue = true;
            }
        } catch (IOException e) {
            Log.e(TAG, "Append two mp4 files exception", e);
        }
        return rvalue;
    }


    public static void copyFile(final String from, final String destination)
            throws IOException {
        FileInputStream in = new FileInputStream(from);
        FileOutputStream out = new FileOutputStream(destination);
        copy(in, out);
        in.close();
        out.close();
    }

    public static void copy(FileInputStream in, FileOutputStream out) throws IOException {
        byte[] buf = new byte[FILE_BUFFER_SIZE];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
    }

    public static void append(
            final String firstFile,
            final String secondFile,
            final String newFile) throws IOException {

        final FileInputStream fisOne = new FileInputStream(new File(secondFile));
        final FileInputStream fisTwo = new FileInputStream(new File(firstFile));
        final FileOutputStream fos = new FileOutputStream(new File(String.format(newFile)));

        Movie movieOne = MovieCreator.build(firstFile);
        Movie movieTwo = MovieCreator.build(secondFile);
        Movie finalMovie = new Movie();


        final List<Track> movieOneTracks = movieOne.getTracks();
        final List<Track> movieTwoTracks = movieTwo.getTracks();

        for (int i = 0; i <movieOneTracks.size() || i < movieTwoTracks.size(); ++i) {
            finalMovie.addTrack(new AppendTrack(movieTwoTracks.get(i), movieOneTracks.get(i)));
        }

        BasicContainer out = (BasicContainer) new DefaultMp4Builder().build(finalMovie);
        out.writeContainer(fos.getChannel());

        fisOne.close();
        fisTwo.close();
        fos.close();
    }


}
