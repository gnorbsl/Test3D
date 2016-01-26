package izm.fraunhofer.de.phoffmn.test3d.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;

import org.rajawali3d.Object3D;
import org.rajawali3d.cameras.ArcballCamera;
import org.rajawali3d.lights.PointLight;
import org.rajawali3d.loader.LoaderOBJ;
import org.rajawali3d.loader.ParsingException;
import org.rajawali3d.materials.Material;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.Sphere;
import org.rajawali3d.renderer.RajawaliRenderer;

import izm.fraunhofer.de.phoffmn.test3d.R;
import izm.fraunhofer.de.phoffmn.test3d.fragments.ModelFragment;


public class Renderer extends RajawaliRenderer {


    private final Context context;

    private Object3D mObjectGroup;

    public ArcballCamera arcball;

    String objName;
    AlertDialog alert;


    public Renderer(Context context, String string, AlertDialog alert) {
        super(context);
        this.context = context;
        setFrameRate(60);
        objName = string;
        this.alert = alert;
    }

    @Override
    protected void initScene() {




        int objId = context.getResources().getIdentifier("raw/" + objName + "_obj",
                "raw", context.getPackageName());

        LoaderOBJ objParser = new LoaderOBJ(context.getResources(), mTextureManager, objId);



        arcball = new ArcballCamera(mContext, ((Activity)mContext).findViewById(R.id.contentview));


        try {
            objParser.parse();

            mObjectGroup = objParser.getParsedObject();
            mObjectGroup.setScale(0.7);


            getCurrentScene().addChild(mObjectGroup);


            ModelFragment modelFragment = (ModelFragment) ((MainActivity)context).getFragmentManager().findFragmentByTag(MainActivity.CONTENT_FRAGMENT);

            modelFragment.enableReset(arcball.getViewMatrix(), arcball.getFieldOfView());




        } catch (ParsingException e) {
            e.printStackTrace();
        }

        Sphere s = new Sphere(0.01f,5,5);

        s.setMaterial(new Material());
        getCurrentScene().addChild(s);




        arcball.setTarget(mObjectGroup);

        Log.d("ARC", " " + arcball.getOrientation());

        arcball.setPosition(0, 0, 4);

        getCurrentScene().replaceAndSwitchCamera(getCurrentCamera(), arcball);

        alert.cancel();

    }

    @Override
    public void onOffsetsChanged(float v, float v1, float v2, float v3, int i, int i1) {


        Log.d("OFF", " " +  v1);

    }

    @Override
    public void onTouchEvent(MotionEvent motionEvent) {
        Log.d("ARC", " " + arcball.getOrientation());
    }

    public Object3D getObject() {
        return mObjectGroup;
    }


}
