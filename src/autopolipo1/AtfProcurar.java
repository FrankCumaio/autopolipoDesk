/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autopolipo1;

import java.util.List;
import javax.swing.JTextField;
import javax.swing.text.*;

public class AtfProcurar extends JTextField {
  class AutoDocument extends PlainDocument {

 

    @Override
    public void insertString(int i, String s, AttributeSet attributeset)
        throws BadLocationException {
      if (s == null || "".equals(s))
        return;
      String s1 = getText(0, i);
      String s2 = getMatch(s1 + s);
      int j = (i + s.length()) - 1;
      if (isStrict && s2 == null) {
        s2 = getMatch(s1);
        j--;
      } else if (!isStrict && s2 == null) {
        super.insertString(i, s, attributeset);
        return;
      }
      
      super.remove(0, getLength());
      super.insertString(0, s2, attributeset);
      setSelectionStart(j + 1);
      setSelectionEnd(getLength());
    }

  }

  public AtfProcurar(List list) {
    isCaseSensitive = false;
    isStrict = true;
    if (list == null) {
      throw new IllegalArgumentException("values can not be null");
    } else {
      dataList = list;
      setDocument(new AutoDocument());
      return;
    }
  }
  private String getMatch(String s) {
    for (int i = 0; i < dataList.size(); i++) {
      String s1 = dataList.get(i).toString();
      if (s1 != null) {
        if (!isCaseSensitive
            && s1.toLowerCase().startsWith(s.toLowerCase()))
          return s1;
        if (isCaseSensitive && s1.startsWith(s))
          return s1;
      }
    }

    return null;
  }

  public boolean isCaseSensitive() {
    return isCaseSensitive;
  }

  public void setCaseSensitive(boolean flag) {
    isCaseSensitive = flag;
  }

  public boolean isStrict() {
    return isStrict;
  }

  public void setStrict(boolean flag) {
    isStrict = flag;
  }

  public List getDataList() {
    return dataList;
  }

  public void setDataList(List list) {
    if (list == null) {
      throw new IllegalArgumentException("values can not be null");
    } else {
      dataList = list;
      return;
    }
  }

  private List dataList;

  private boolean isCaseSensitive;

  private boolean isStrict;

  //private Java2sAutoComboBox autoComboBox;
}