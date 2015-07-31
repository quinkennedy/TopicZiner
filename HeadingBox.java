

class HeadingBox extends TextBox{
  float headingSize, subheadingSize;
  XML heading, subheading;
  boolean hasHeading, hasSubheading;
  Map<String, String> vars;
  
  public HeadingBox(XML _heading, XML _subheading, FontFamily fnt, float _headingSize, 
      float _subheadingSize, PGraphics pg, Map<String, String> _vars, boolean adjustSize){
    heading = _heading;
    subheading = _subheading;
    font = fnt;
    vars = _vars;
    headingSize = _headingSize;
    subheadingSize = _subheadingSize;
    text = new FormattedTextBlock(pg);
    parse(_heading, fnt, FontWeight.REGULAR, FontEm.REGULAR, _headingSize, vars, text);
    int numTexts = text.text.size();
    hasHeading = numTexts > 0;
    if (hasHeading){
      text.add("\n", fnt.getReg(), _subheadingSize);
    }
    parse(_subheading, fnt, FontWeight.LIGHT, FontEm.REGULAR, _subheadingSize, vars, text);
    hasSubheading = text.text.size() > numTexts;
    if (!hasSubheading){
      text.dropLast();
    }
    adjustFontSize = adjustSize;
  }
  public boolean isResizable(){
    return false;
  }
  private boolean hasHeading(){
    return hasHeading;
  }
  private boolean hasSubheading(){
    return hasSubheading;
  }
  public String getHeadingText(PGraphics pg){
    if(hasHeading){
      FormattedTextBlock hBlock = new FormattedTextBlock(pg);
      parse(heading, font, FontWeight.REGULAR, FontEm.REGULAR, headingSize, vars, hBlock);
      return hBlock.getString();
    } else {
      return null;
    }
    
  }
  private float getHeadingSize(){
    if (!hasHeading()){
      return 0;
    } else {
      return text.text.get(0).fontSize;
    }
  }
  private float getSubheadingSize(){
    if (!hasSubheading()){
      return 0;
    } else {
      return text.text.get(text.text.size() - 1).fontSize;
    }
  }
}