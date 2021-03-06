package udacity.com.br.popularmovies.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import udacity.com.br.popularmovies.R;
import udacity.com.br.popularmovies.model.Movies;
import udacity.com.br.popularmovies.util.Constant;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {


    public DetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        TextView mOriginalTitle = (TextView) view.findViewById(R.id.textView_fdetail_original_title);
        TextView mReleaseDate = (TextView) view.findViewById(R.id.textView_fdetail_release_date);
        ImageView mPoster = (ImageView) view.findViewById(R.id.imageView_fdetail_poster);
        TextView mSynopsis = (TextView) view.findViewById(R.id.textView_fdetail_synopsis);
        RatingBar mUserRating = (RatingBar) view.findViewById(R.id.ratingBar_fdetail_user_rating);

        Bundle bundle = getActivity().getIntent().getExtras();
        Movies movies = bundle.getParcelable(Constant.INTENT_MAIN_MOVIE);

        if (movies != null) {

            mOriginalTitle.setText(movies.getOriginalTitle());
            mReleaseDate.setText(movies.getReleaseDate());
            mSynopsis.setText(movies.getSynopsis());

            mUserRating.setMax(10);
            mUserRating.setRating(Float.valueOf(movies.getUserRating()) / 2);

            Picasso.with(getActivity()).load(Constant.TMDB_POSTER_IMG + movies.getMoviePoster()).into(mPoster);
        }

        return view;

    }

}
