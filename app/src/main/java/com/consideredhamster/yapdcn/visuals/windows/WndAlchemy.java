package com.consideredhamster.yapdcn.visuals.windows;

import com.consideredhamster.yapdcn.Badges;
import com.consideredhamster.yapdcn.Dungeon;
import com.consideredhamster.yapdcn.Statistics;
import com.consideredhamster.yapdcn.items.Item;
import com.consideredhamster.yapdcn.items.food.Food;
import com.consideredhamster.yapdcn.items.food.MeatRaw;
import com.consideredhamster.yapdcn.items.food.MeatStewed;
import com.consideredhamster.yapdcn.items.herbs.Herb;
import com.consideredhamster.yapdcn.items.potions.EmptyBottle;
import com.consideredhamster.yapdcn.items.potions.Potion;
import com.consideredhamster.yapdcn.items.potions.UnstablePotion;
import com.consideredhamster.yapdcn.levels.Terrain;
import com.consideredhamster.yapdcn.scenes.GameScene;
import com.consideredhamster.yapdcn.scenes.PixelScene;
import com.consideredhamster.yapdcn.visuals.Assets;
import com.consideredhamster.yapdcn.visuals.DungeonTilemap;
import com.consideredhamster.yapdcn.visuals.effects.Speck;
import com.consideredhamster.yapdcn.visuals.ui.Icons;
import com.consideredhamster.yapdcn.visuals.ui.ItemButton;
import com.consideredhamster.yapdcn.visuals.ui.ItemSlot;
import com.consideredhamster.yapdcn.visuals.ui.RedButton;
import com.consideredhamster.yapdcn.visuals.ui.RenderedTextMultiline;
import com.consideredhamster.yapdcn.visuals.ui.Window;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;

import java.util.ArrayList;

import static com.consideredhamster.yapdcn.scenes.PixelScene.align;

public class WndAlchemy extends Window{

    public static final int MODE_BREW		= 1;
    public static final int MODE_COOK       = 2;

    private static final float GAP		    = 2;
    private static final int WIDTH		    = 116;

    private ItemButton[] inputs;// = new WndBlacksmith.ItemButton[2];

    private RedButton btnCombine;

    private ItemSlot output;
    private ItemSlot baseItemSlot;

    private Item baseItem;

    private Emitter smokeEmitter;
    private Emitter bubbleEmitter;

    private int mode;

    private static final int BTN_SIZE	= 28;

    public WndAlchemy( int mode ){

        super();
        this.mode = mode;

        IconTitle titlebar = new IconTitle();
        titlebar.icon( DungeonTilemap.tile( Terrain.ALCHEMY ) );
        titlebar.label( "炼金釜" );
        titlebar.setRect( 0, 0, WIDTH, 0 );
        add( titlebar );

        int h = 0;

        h += titlebar.height() + GAP;

        String hint = this.mode == MODE_BREW ?
                "选择两份草药并将萃取液装入空瓶。" :
                "你可以在烹饪生肉时选择一个用于调味的草药。";

        RenderedTextMultiline message = PixelScene.renderMultiline( hint, 6 );

        message.maxWidth(WIDTH);
        align(message);

        message.setPos(0,h);
        add( message );

        h += message.height() + GAP;

        int w = WIDTH;

        inputs = this.mode == MODE_BREW ?
            new ItemButton[2] :
            new ItemButton[1] ;

        int lastBtnIndex = inputs.length - 1;

        for (int i = 0; i < inputs.length; i++) {
            inputs[i] = new ItemButton(){
                @Override
                protected void onClick() {

                    super.onClick();

                    if( item != null ){

                        if (!item.collect()){
                            Dungeon.level.drop(item, Dungeon.hero.pos);
                        }

                        item = null;

                        slot.clearIcon();
                        slot.item = null;

                        //slot.item(new WndBag.Placeholder(ItemSpriteSheet.SOMETHING));
                    }

                    GameScene.selectItem( itemSelector, WndBag.Mode.HERB, "选择" );
                }
            };

            inputs[i].setRect(15, h, BTN_SIZE, BTN_SIZE);
            add(inputs[i]);
            h += BTN_SIZE + 2;
        }

        updateBaseItem();

        baseItemSlot = new ItemSlot( baseItem ){
            @Override
            protected void onClick() {
                super.onClick();
                if ( visible && item.name() != null ){
                    GameScene.show( new WndInfoItem( item ) );
                }
            }
            @Override
            public void item( Item item ) {
                super.item( item );
                topLeft.text( Integer.toString( item.quantity ) );
                topLeft.visible = true;
            }
        };

        baseItemSlot.setRect( 15, inputs[ lastBtnIndex ].bottom() + 2, BTN_SIZE, BTN_SIZE );

        ColorBlock baseItemBG = new ColorBlock( baseItemSlot.width(), baseItemSlot.height(), 0x9991938C );

        baseItemBG.x = baseItemSlot.left();
        baseItemBG.y = baseItemSlot.top();

        add(baseItemBG);
        add(baseItemSlot);

        h += BTN_SIZE + 2;

        Image arrow = Icons.get(Icons.RESUME);
        arrow.hardlight( 20, 20, 20 );
        arrow.x = (w - arrow.width)/2f;
        arrow.y = baseItemSlot.top() + (baseItemSlot.height() - arrow.height)/2f;
        //PixelScene.align(arrow);
        add(arrow);

        output = new ItemSlot(){
            @Override
            protected void onClick() {
                super.onClick();
                if ( visible && item.name() != null ){
                    GameScene.show( new WndInfoItem( item ) );
                }
            }
        };

        output.setRect(w - BTN_SIZE - 15, baseItemSlot.top(), BTN_SIZE, BTN_SIZE);

        ColorBlock outputBG = new ColorBlock(output.width(), output.height(), 0x9991938C);
        outputBG.x = output.left();
        outputBG.y = output.top();
        add(outputBG);

        add(output);
        output.visible = false;

        bubbleEmitter = new Emitter();
        smokeEmitter = new Emitter();

        bubbleEmitter.pos(
            outputBG.x + ( BTN_SIZE - 16 ) / 2f,
            outputBG.y + ( BTN_SIZE - 16 ) / 2f,
        16, 16 );

        smokeEmitter.pos(
            bubbleEmitter.x, bubbleEmitter.y,
            bubbleEmitter.width, bubbleEmitter.height
        );

        bubbleEmitter.autoKill = false;
        smokeEmitter.autoKill = false;

        add(bubbleEmitter);
        add(smokeEmitter);

        h += 4;
        float btnWidth = ( w - 14 ) / 2f;

        btnCombine = new RedButton( this.mode == MODE_BREW ? "精炼" : "烹饪" ){
            @Override
            protected void onClick() {
                super.onClick();
                combine();
            }
        };

        btnCombine.setRect(5, h, btnWidth, 18);
        //PixelScene.align(btnCombine);
        btnCombine.enable(false);
        add(btnCombine);

        RedButton btnCancel = new RedButton( "取消"){
            @Override
            protected void onClick() {
                super.onClick();
                onBackPressed();
            }
        };
        btnCancel.setRect(w - 5 - btnWidth, h, btnWidth, 18);
        //PixelScene.align(btnCancel);
        add(btnCancel);

        h += btnCancel.height();

        resize(w, h);

        updateState();

    }

    private void updateBaseItem(){

        if( this.mode == MODE_BREW ){

            baseItem = Dungeon.hero.belongings.getItem( EmptyBottle.class );

            if( baseItem == null ) {
                baseItem = new EmptyBottle().quantity( 0 );
            }

        } else {

            baseItem = Dungeon.hero.belongings.getItem( MeatRaw.class );
            if( baseItem == null ){
                baseItem = new MeatRaw().quantity( 0 );
            }

        }
    }

    protected WndBag.Listener itemSelector = new WndBag.Listener() {
        @Override
        public void onSelect( Item item ) {

            if (item != null) {

                for ( ItemButton input : inputs ) {
                    if (input.item == null){
                        input.item(item.detach(Dungeon.hero.belongings.backpack));
                        break;
                    }
                }
            }
            updateState();
        }
    };

    private void updateState(){

        if( mode == MODE_BREW && baseItem.quantity() > 0 && filterInput( Herb.class).size() == 2 ){

            //potion creation requires two herbs, there are no alternatives (for now?)

            Potion potion = getBrewResult();

            if( potion != null ){

                potion.dud = true;

                output.item( potion );
                output.visible = true;

                btnCombine.enable( true );

            } else {

                btnCombine.enable(false);
                output.visible = false;

            }

        } else if( mode == MODE_COOK && baseItem.quantity() > 0 ){

            // we can cook meat without any herbs, getting a simple stewed meat in the process

            output.item( getCookResult());
            output.visible = true;

            btnCombine.enable(true);

        } else {

            btnCombine.enable(false);
            output.visible = false;

        }
    }

    private void combine(){

        ArrayList<Herb> herbs = filterInput(Herb.class);

        Item result = null;

        if ( mode == MODE_BREW && baseItem.quantity() > 0 && herbs.size() == 2 ){

            //potion creation
            result = getBrewResult();

            baseItem.detach(Dungeon.hero.belongings.backpack);
            baseItem = Dungeon.hero.belongings.getItem( EmptyBottle.class );

            if( baseItem == null ){
                baseItem = new EmptyBottle().quantity( 0 );
            }

            Statistics.potionsCooked++;
            Badges.validatePotionsCooked();

        } else if( mode == MODE_COOK && baseItem.quantity() > 0 ) {

            //meat cooking
            result = getCookResult();

            baseItem.detach(Dungeon.hero.belongings.backpack);
            baseItem = Dungeon.hero.belongings.getItem( MeatRaw.class );

            if( baseItem == null ){
                baseItem = new MeatRaw().quantity( 0 );
            }

        }

        if (result != null){

            result.identify();
            output.item( result );

            if (!result.collect()){
                Dungeon.level.drop(result, Dungeon.hero.pos);
            }

            for (int i = 0; i < inputs.length; i++){
                inputs[i].clear();
                inputs[i].item = null;
            }

            baseItemSlot.item(baseItem);

            if( mode == MODE_BREW || baseItem == null || baseItem.quantity == 0 ){
                btnCombine.enable( false );
            }

            bubbleEmitter.start(Speck.factory( Speck.BUBBLE ), 0.2f, 10 );
            smokeEmitter.burst(Speck.factory( Speck.WOOL ), 10 );
            Sample.INSTANCE.play( Assets.SND_PUFF );

        }
    }

    @Override
    public void onBackPressed() {
        for (int i = 0; i < inputs.length; i++) {
            if (inputs[i].item != null){
                if (!inputs[i].item.collect()){
                    Dungeon.level.drop(inputs[i].item, Dungeon.hero.pos);
                }
            }
        }
        super.onBackPressed();
    }

    private Food getCookResult(){

        Item item = inputs[0].item;

        try{
            if (item instanceof Herb){
                return ((Herb)item).cooking.newInstance();
            }
        } catch ( InstantiationException e ) {
            e.printStackTrace();
        } catch ( IllegalAccessException e ) {
            e.printStackTrace();
        }

        return new MeatStewed();
    }

    private Potion getBrewResult() {

        // this algorithm is much simpler =P
        // (except for the ugly try/catch)

        Herb herb1 = (Herb)inputs[0].item;
        Herb herb2 = (Herb)inputs[1].item;

        try{

            if( herb1.getClass() == herb2.getClass() ) {
                return herb1.mainPotion.newInstance();
            }

            for( Class<? extends Potion> check : herb1.subPotions ){
                for( Class<? extends Potion> with : herb2.subPotions ){
                    if( check == with ){
                        return check.newInstance();
                    }
                }
            }

            return new UnstablePotion();

        } catch ( InstantiationException e ) {
            e.printStackTrace();
        } catch ( IllegalAccessException e ) {
            e.printStackTrace();
        }

        return null;
    }

    private<T extends Item> ArrayList<T> filterInput(Class<? extends T> itemClass){
        ArrayList<T> filtered = new ArrayList<>();
        for (int i = 0; i < inputs.length; i++){
            Item item = inputs[i].item;
            if (item != null && itemClass.isInstance(item)){
                filtered.add((T)item);
            }
        }
        return filtered;
    }
}
