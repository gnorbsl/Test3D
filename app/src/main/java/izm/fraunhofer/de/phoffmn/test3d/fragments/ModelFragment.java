package izm.fraunhofer.de.phoffmn.test3d.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.rajawali3d.cameras.ArcballCamera;
import org.rajawali3d.math.Matrix4;
import org.rajawali3d.math.Quaternion;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.surface.IRajawaliSurface;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import izm.fraunhofer.de.phoffmn.test3d.R;
import izm.fraunhofer.de.phoffmn.test3d.activities.Renderer;

/**
 * A simple {@link Fragment} subclass.
 */
public class ModelFragment extends Fragment{


    private Renderer mRenderer;

    Button resetView;
    private Matrix4 viewMatrix;
    private ImageButton top, bottom, left, right;

    public ModelFragment() {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRenderer.getObject().setY(mRenderer.getObject().getY() + .1);
            }
        });

        bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRenderer.getObject().setY(mRenderer.getObject().getY() - .1);
            }
        });


        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRenderer.getObject().setX(mRenderer.getObject().getX() + .1);
            }
        });

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRenderer.getObject().setX(mRenderer.getObject().getX() - .1);
            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RelativeLayout mLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_model, container, false);

        top = (ImageButton) mLayout.findViewById(R.id.top);
        bottom = (ImageButton) mLayout.findViewById(R.id.bottom);
        left = (ImageButton) mLayout.findViewById(R.id.left);
        right = (ImageButton) mLayout.findViewById(R.id.right);

        resetView = (Button) mLayout.findViewById(R.id.resetView);


        // Find the TextureView
        IRajawaliSurface mRajawaliSurface = (IRajawaliSurface) mLayout.findViewById(R.id.rajwali_surface);

        mRenderer = new Renderer(getActivity());

        mRajawaliSurface.setSurfaceRenderer(mRenderer);


        resetView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TEST", "click");

                mRenderer.getObject().setX(0);
                mRenderer.getObject().setY(0);
                mRenderer.getObject().setZ(0);

                Log.d("BLA", mRenderer.getCurrentCamera().getRotX() + "");



            }
        });



        return mLayout;
    }



    public void enableReset(Matrix4 viewMatrix) {


        this.viewMatrix = viewMatrix;

        getActivity().runOnUiThread(() -> resetView.setEnabled(true));
    }
}
