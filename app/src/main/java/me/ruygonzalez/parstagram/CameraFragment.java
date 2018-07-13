package me.ruygonzalez.parstagram;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class CameraFragment extends Fragment {
    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.

    /**
     * A callback which is to be implemented by the context which contains this activity. In our
     * case the context will be our containing activity.
     */
    interface Callback {
        /**
         * This method will be implemented by my activity, and my fragment will call this
         * method when there is a text change event.
         */
        void onTextChanged(@NonNull final String text);
        void clicked();
        void makeProfilePic();
    }

    /**
     * Reference to something that implements my Callback.
     */
    private Callback inputCallback;
    private Callback clickCallback;

    ImageView ivPhoto;
    EditText etDescription;
    Button btnPost;
    Button btnPostProfile;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // `instanceof` here is how we check if the containing context (in our case the activity)
        // implements the required callback interface.
        //
        // If it does not implement the required callback, we want
        if (context instanceof Callback) {

            // If it is an instance of our Callback then we want to cast the context to a Callback
            // and store it as a reference so we can later update the callback when there has been
            // a text change event.
            inputCallback = (Callback) context;
            clickCallback = (Callback) context;
        } else {
            // Throwing an error and making your application crash instead of just sweeping it under
            // the rug is called being an "offensive" programmer.
            //
            // The best defense is a strong offense.
            throw new IllegalStateException("Containing context must implement CameraFragment.Callback.");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        View v = inflater.inflate(R.layout.fragment_camera, parent, false);
        ivPhoto = v.findViewById(R.id.ivPhoto);
        return v;
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
        etDescription = (EditText) view.findViewById(R.id.description_et);
        etDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // unused but required method
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // unused but required method
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Will pass back the text change to my "inputCallback" which is my
                // activity in this case, since we casted it in onAttach(context)
                //
                // This is how we communicate upwards from a fragment -> a containing activity.
                // we use a "listener" or "callback" pattern to do this.
                inputCallback.onTextChanged(s.toString());
            }
        });
        btnPost = (Button) view.findViewById(R.id.btnPost);
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickCallback.clicked();
            }
        });
        btnPostProfile = (Button) view.findViewById(R.id.btnPostProfile);
        btnPostProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickCallback.makeProfilePic();
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Because we grabbed a reference to our containing context in on attach, it is approriate
        // to clean-up our references in onDetach() so that way we don't leak any references and
        // run into any odd runtime errors!
        inputCallback = null;
        clickCallback = null;
    }
}