
package co.jlabs.famb;

import co.jlabs.famb.R;
import co.jlabs.famb.frag.AnimationSet;

/**
 * Default AnimationSets for the App
 */

class Anims {

    static class SlideFromRight extends AnimationSet {
        SlideFromRight() {
            super(R.anim.slide_from_right, R.anim.slide_to_left, R.anim.slide_from_left, R.anim.slide_to_right);
        }
    }
    static class SlideFromBottom extends AnimationSet {
        SlideFromBottom() {
            super(R.anim.slide_from_bottom, R.anim.slide_to_top, R.anim.slide_from_top, R.anim.slide_to_bottom);
        }
    }
}
