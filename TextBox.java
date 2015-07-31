class TextBox extends ContentBox{
  FormattedTextBlock text;
  FontFamily font;
  float fontSize;
  boolean adjustFontSize = false;
  
  private TextBox(){}
  
  public TextBox(XML txt, FontFamily fnt, float size, PGraphics pg, Map<String, String> vars, boolean adjustSize){
    font = fnt;
    fontSize = size;
    text = new FormattedTextBlock(pg);
    parse(txt, fnt, FontWeight.REGULAR, FontEm.REGULAR, size, vars, text);
    adjustFontSize = adjustSize;
  }
  
  public TextBox(String txt, PFont fnt, float size, PGraphics pg, boolean adjustSize){
    fontSize = size;
    text = new FormattedTextBlock(pg);
    text.add(txt, fnt, size);
    adjustFontSize = adjustSize;
  }
  
  public boolean isResizable(){
    return adjustFontSize;
  }
  
  public Rectangle render(Rectangle area, PGraphics pg, boolean debug){
    text.setConstraints(area.w, area.h);
    pg.pushMatrix();
    pg.translate(area.x, area.y);
    text.render(pg, debug);
    pg.popMatrix();
    Rectangle used = new Rectangle(area.x, area.y, text.getWidth(), text.getHeight());
    return used;
  }
  
  //so what I want is a recursive function which edits a list and returns a string.
  //if it gets a string back, then it concatinates that string to it's own string, if there
  //  were nodes added to the List after this function was entered, add this function's text to the List
  protected void parse(XML txt, FontFamily fnt, FontWeight weight, FontEm em, 
      float size, Map<String, String> vars, FormattedTextBlock block){
    LinkedList<FormattedTextBlock.FormattedText> bits = 
      new LinkedList<FormattedTextBlock.FormattedText>();
    String result = parse(bits, txt, fnt, weight, em, size, vars);
    if (result != null && result.length() > 0){
      bits.add(new FormattedTextBlock.FormattedText(result, fnt.get(weight, em), size));
    }
    for(int i = 0; i < bits.size(); i++){
      block.add(bits.get(i));
    }
  }
  
  private String parse(
      LinkedList<FormattedTextBlock.FormattedText> bits, XML node, FontFamily fnt, 
      FontWeight weight, FontEm em, float size, Map<String, String> vars){
    FontWeight myWeight = weight;
    FontEm myEm = em;
    if (node == null){
      return null;
    }
    String currName = node.getName();
    if (currName.equals("#text")){
      return node.getContent();
    } else if (currName.equals("var")){
      if (vars.containsKey(node.getString("key"))){
        return vars.get(node.getString("key"));
      } else {
        return node.format(-1);
      }
    } else if (currName.equals("bold")){
      myWeight = FontWeight.BOLD;
    } else if (currName.equals("italic")){
      myEm = FontEm.ITALIC;
    }
    XML[] children = node.getChildren();
    String myText = "";
    int listLength = bits.size();
    for(XML child : children){
      String childText = parse(bits, child, fnt, myWeight, myEm, size, vars);
      if (bits.size() > listLength){
        if (myText.length() > 0){
          bits.add(listLength, new FormattedTextBlock.FormattedText(myText, fnt.get(myWeight, myEm), size));
          if (childText != null){
            myText = childText;
          } else {
            myText = "";
          }
        }
        listLength = bits.size();
      } else if (childText != null && childText.length() > 0){
        myText += childText;
      }
    }
    if (myText.length() > 0){
      bits.add(new FormattedTextBlock.FormattedText(myText, fnt.get(myWeight, myEm), size));
    }
    return null;
  }
}