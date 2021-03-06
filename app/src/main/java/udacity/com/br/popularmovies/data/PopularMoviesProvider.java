
package udacity.com.br.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

import udacity.com.br.popularmovies.data.PopularMoviesContract.MovieEntry;

public class PopularMoviesProvider extends ContentProvider {


    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private PopularMoviesDbHelper mOpenHelper;

    static final int MOVIES = 100;
    static final int MOVIES_WITH_ID = 101;

    private static final SQLiteQueryBuilder sMoviesQueryBuilder;

    static{
        sMoviesQueryBuilder = new SQLiteQueryBuilder();
        sMoviesQueryBuilder.setTables(MovieEntry.TABLE_NAME );
    }

    private static final String sMoviesByIdSelection =
            MovieEntry.TABLE_NAME+
                    "." + MovieEntry._ID + " = ? ";


    private Cursor getMovies(Uri uri,String sortOrder) {

        String[] projection = {MovieEntry._ID,
                               MovieEntry.COLUMN_MOVIE_TITLE,
                               MovieEntry.COLUMN_MOVIE_RELEASE_DATE,
                               MovieEntry.COLUMN_MOVIE_RATING,
                               MovieEntry.COLUMN_MOVIE_REVIEW,
                               MovieEntry.COLUMN_MOVIE_POSTER
        };

        return sMoviesQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );
    }


    static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = PopularMoviesContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, PopularMoviesContract.PATH_FAVORITE_MOVIES, MOVIES);
        matcher.addURI(authority, PopularMoviesContract.PATH_FAVORITE_MOVIES + "/*", MOVIES_WITH_ID);

        return matcher;
    }


    @Override
    public boolean onCreate() {
        mOpenHelper = new PopularMoviesDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }


    @Override
    public String getType(Uri uri) {

        final int match = sUriMatcher.match(uri);

        switch (match) {
            case MOVIES:
                return MovieEntry.CONTENT_TYPE;
            case MOVIES_WITH_ID:
                return MovieEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case MOVIES: {
                long _id = db.insert(MovieEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = MovieEntry.buildMoviesUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri,String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;

        switch (match) {
            case MOVIES_WITH_ID:
                String movieId = MovieEntry.getMovieIDFromUri(uri);
                selection = MovieEntry._ID +" = ?";
                selectionArgs = new String[]{movieId};
                rowsDeleted = db.delete(
                        MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }


}