package de.lessvoid.nifty.controls.textfield.filter.delete;

import javax.annotation.Nonnull;

/**
 * This delete filter is the default filter that simply delete all characters requested without asking further
 * questions.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class FilterDeleteAll implements TextFieldDeleteFilter {
  @Override
  public boolean acceptDelete(@Nonnull final CharSequence oldSequence, final int deleteStart, final int deleteEnd) {
    return true;
  }
}
