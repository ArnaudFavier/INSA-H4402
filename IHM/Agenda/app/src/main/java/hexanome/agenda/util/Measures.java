package hexanome.agenda.util;

import android.content.res.Resources;
import android.util.TypedValue;

public final class Measures {
    private Measures() {
    }

    public static final float dpToPixels(Resources r, float dp) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
        return px;
    }
}
