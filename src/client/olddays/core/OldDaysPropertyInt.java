package net.minecraft.src;

public class OldDaysPropertyInt extends OldDaysProperty{
    public int value;
    public int smpValue;
    public int count;
    private String[] names;
    public boolean useNames;

    public OldDaysPropertyInt(OldDaysModule m, int i, String s, int v, int c, String f){
        super(m, i, s, TYPE_INTEGER, f);
        value = v;
        guitype = GUI_TYPE_BUTTON;
        count = c;
        names = null;
        useNames = false;
    }

    public OldDaysPropertyInt(OldDaysModule m, int i, String s, int v, int c, int smp, String f){
        this(m, i, s, v, c, f);
        smpValue = smp;
        allowedInSMP = false;
    }

    public void setNames(String[] str){
        names = str;
        useNames = true;
    }

    public String getButtonText(){
        return name+": "+(useNames ? names[value] : value);
    }

    public void onChange(){
        if (isDisabled()){
            return;
        }
        try{
            field.set(module, value);
            module.callback(id);
        }catch(Exception ex){
            System.out.println(ex);
            disable();
        }
    }

    public void incrementValue(){
        if (value < count - 1){
            value++;
        }else{
            value = 0;
        }
    }

    public void setSMPValue(){
        value = smpValue;
    }

    protected void disable(){
        super.disable();
        value = smpValue;
    }

    public void loadFromString(String str){
        value = Integer.parseInt(str);
    }

    public String saveToString(){
        return ""+value;
    }
}