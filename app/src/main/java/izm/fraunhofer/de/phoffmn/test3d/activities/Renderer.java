package izm.fraunhofer.de.phoffmn.test3d.activities;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;

import org.rajawali3d.Object3D;
import org.rajawali3d.cameras.ArcballCamera;
import org.rajawali3d.lights.PointLight;
import org.rajawali3d.loader.LoaderOBJ;
import org.rajawali3d.loader.ParsingException;
import org.rajawali3d.materials.Material;
import org.rajawali3d.primitives.Sphere;
import org.rajawali3d.renderer.RajawaliRenderer;

import izm.fraunhofer.de.phoffmn.test3d.R;
import izm.fraunhofer.de.phoffmn.test3d.fragments.ModelFragment;


public class Renderer extends RajawaliRenderer {


    private final Context context;

    private Object3D mObjectGroup;

    public ArcballCamera arcball;


    public Renderer(Context context) {
        super(context);
        this.context = context;
        setFrameRate(60);
    }

    @Override
    protected void initScene() {


        LoaderOBJ objParser = new LoaderOBJ(context.getResources(), mTextureManager, R.raw.tamb100_obj);

        PointLight mLight = new PointLight();
        mLight.setPosition(4, 0, 4);
        mLight.setPower(3);

        PointLight mLight2 = new PointLight();
        mLight.setPosition(-4, 0, -4);
        mLight.setPower(3);

        getCurrentScene().addLight(mLight);
        getCurrentScene().addLight(mLight2);

        arcball = new ArcballCamera(mContext, ((Activity)mContext).findViewById(R.id.contentview));

        try {
            objParser.parse();
            mObjectGroup = objParser.getParsedObject();


            getCurrentScene().addChild(mObjectGroup);


            ModelFragment modelFragment = (ModelFragment) ((MainActivity)context).getFragmentManager().findFragmentByTag(MainActivity.CONTENT_FRAGMENT);

            modelFragment.enableReset(arcball.getViewMatrix());




        } catch (ParsingException e) {
            e.printStackTrace();
        }
        Sphere s = new Sphere(0.02f,5,5);
        s.setMaterial(new Material());
        getCurrentScene().addChild(s);




        arcball.setTarget(mObjectGroup);

        Log.d("ARC", " " + arcball.getOrientation());

        arcball.setPosition(0,0,4);

        getCurrentScene().replaceAndSwitchCamera(getCurrentCamera(), arcball);

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
