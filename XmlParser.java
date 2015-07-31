class XmlParser{
  private XmlParser();
  
  public static String parseToString(XML xText, Map<String, String> vars, PGraphics pg){
    FormattedTextBlock block = new FormattedTextBlock(pg);
    parseToBlock(xText, null, FontFamily.FontWeight.REGULAR, FontFamily.FontEm.REGULAR, 12, vars, block);
    for(int i = 0; i < block.text.count(); i++){
    }
  }
  
  public static void parseToBlock(XML xText, FontFamily fontFam, 
      FontWeight weight, FontEm em, float size, Map<String, String> vars, FormattedTextBlock block){
    //parse it!
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
  
  private static String parse(
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