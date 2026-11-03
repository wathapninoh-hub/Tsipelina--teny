package mg.tsipelina.translator;

import android.app.Activity;
import android.os.Bundle;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.widget.*;

public class MainActivity extends Activity {
    private EditText input;
    private TextView output;
    private final int purple = Color.rgb(105, 83, 232);
    private int dp(int n) { return (int)(n * getResources().getDisplayMetrics().density + 0.5f); }

    private TextView label(String s, int size, int color, boolean bold) {
        TextView t = new TextView(this);
        t.setText(s); t.setTextSize(size); t.setTextColor(color);
        if (bold) t.setTypeface(null, android.graphics.Typeface.BOLD);
        return t;
    }
    private LinearLayout card() {
        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(dp(18), dp(18), dp(18), dp(18));
        GradientDrawable bg = new GradientDrawable();
        bg.setColor(Color.WHITE); bg.setCornerRadius(dp(20));
        box.setBackground(bg);
        return box;
    }
    private Button makeButton(String s, int color) {
        Button b = new Button(this);
        b.setText(s); b.setTextColor(Color.WHITE); b.setTextSize(14); b.setAllCaps(false);
        GradientDrawable bg = new GradientDrawable();
        bg.setColor(color); bg.setCornerRadius(dp(15)); b.setBackground(bg);
        return b;
    }

    // Ny litera tsirairay dia manana litera mpiara-mivadika aminy.
    private String translateText(String source) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < source.length(); i++) {
            char c = source.charAt(i);
            boolean upper = Character.isUpperCase(c);
            char lower = Character.toLowerCase(c);
            char mapped = switch (lower) {
                case 'b' -> 'z'; case 'z' -> 'b';
                case 'd' -> 'v'; case 'v' -> 'd';
                case 'r' -> 'h'; case 'h' -> 'r';
                case 'm' -> 'l'; case 'l' -> 'm';
                case 'n' -> 'k'; case 'k' -> 'n';
                case 't' -> 'f'; case 'f' -> 't';
                case 'p' -> 'j'; case 'j' -> 'p';
                case 'g' -> 's'; case 's' -> 'g';
                default -> lower;
            };
            result.append(upper ? Character.toUpperCase(mapped) : mapped);
        }
        return result.toString();
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.rgb(20,24,48));
        getWindow().setNavigationBarColor(Color.rgb(20,24,48));
        ScrollView scroll = new ScrollView(this);
        LinearLayout page = new LinearLayout(this);
        page.setOrientation(LinearLayout.VERTICAL);
        page.setPadding(dp(22), dp(25), dp(22), dp(28));
        page.setBackground(new GradientDrawable(GradientDrawable.Orientation.TL_BR,
            new int[]{Color.rgb(20,24,48), Color.rgb(46,42,91)}));
        scroll.addView(page);

        TextView brand = label("✦  TSIPELINA", 13, Color.rgb(195,184,255), true);
        brand.setLetterSpacing(0.12f); page.addView(brand);
        TextView title = label("Malagasy ho\namin'ny Tsipelina", 29, Color.WHITE, true);
        title.setPadding(0, dp(15), 0, dp(7)); page.addView(title);
        page.addView(label("Fandikana teny tsotra sy haingana", 14, Color.rgb(208,210,230), false));

        LinearLayout inputCard = card();
        LinearLayout.LayoutParams cp = new LinearLayout.LayoutParams(-1,-2);
        cp.topMargin = dp(27); page.addView(inputCard,cp);
        inputCard.addView(label("SORATY ETO NY TENY MALAGASY",11,purple,true));
        input = new EditText(this);
        input.setHint("Ohatra: Manao ahoana ianao?");
        input.setTextSize(17); input.setTextColor(Color.rgb(28,33,55));
        input.setHintTextColor(Color.rgb(145,149,165));
        input.setGravity(Gravity.TOP | Gravity.START); input.setMinLines(4);
        input.setPadding(0,dp(13),0,dp(8)); input.setBackgroundColor(Color.TRANSPARENT);
        inputCard.addView(input,new LinearLayout.LayoutParams(-1,-2));

        Button translate = makeButton("ADIKAO  ✦",purple);
        LinearLayout.LayoutParams tp = new LinearLayout.LayoutParams(-1,dp(54));
        tp.topMargin=dp(15); page.addView(translate,tp);

        LinearLayout resultCard=card();
        LinearLayout.LayoutParams rp=new LinearLayout.LayoutParams(-1,-2);
        rp.topMargin=dp(19); page.addView(resultCard,rp);
        resultCard.addView(label("VALINY • TSIPELINA",11,purple,true));
        output=label("Hiseho eto ny dikanteny…",19,Color.rgb(28,33,55),true);
        output.setMinHeight(dp(90)); output.setPadding(0,dp(16),0,dp(10));
        resultCard.addView(output,new LinearLayout.LayoutParams(-1,-2));

        Button copy=makeButton("HANDIKA NY VALINY",Color.rgb(52,60,96));
        LinearLayout.LayoutParams cop=new LinearLayout.LayoutParams(-1,dp(50));
        cop.topMargin=dp(12); page.addView(copy,cop);

        TextView rules=label("FITSIPIKA MIFAMADIKA\nB ⇄ Z    D ⇄ V    R ⇄ H    M ⇄ L\nN ⇄ K    T ⇄ F    P ⇄ J    G ⇄ S\nNy litera hafa rehetra dia tsy miova.",13,Color.rgb(218,218,237),false);
        rules.setGravity(Gravity.CENTER); rules.setPadding(0,dp(24),0,0); page.addView(rules);

        translate.setOnClickListener(v -> {
            String value=input.getText().toString();
            if(value.trim().isEmpty()){input.setError("Ampidiro aloha ny teny Malagasy");return;}
            output.setText(translateText(value));
        });
        copy.setOnClickListener(v -> {
            String value=output.getText().toString();
            if(value.equals("Hiseho eto ny dikanteny…")){
                Toast.makeText(this,"Adikao aloha ny lahatsoratra",Toast.LENGTH_SHORT).show();return;
            }
            ClipboardManager cb=(ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
            cb.setPrimaryClip(ClipData.newPlainText("Tsipelina",value));
            Toast.makeText(this,"Voadika!",Toast.LENGTH_SHORT).show();
        });
        setContentView(scroll);
    }
}
