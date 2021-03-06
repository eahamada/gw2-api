package gw2.api.v2;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.Transaction;
import org.neo4j.driver.v1.Values;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.JsonSchemaGenerator;

import gw2.api.v2.items.ApplyCount;
import gw2.api.v2.items.ArmorDetails;
import gw2.api.v2.items.ArmorSlotType;
import gw2.api.v2.items.Attribute;
import gw2.api.v2.items.AttributeBonus;
import gw2.api.v2.items.BackDetails;
import gw2.api.v2.items.BagDetails;
import gw2.api.v2.items.Bonus;
import gw2.api.v2.items.Buff;
import gw2.api.v2.items.Charges;
import gw2.api.v2.items.ChatLink;
import gw2.api.v2.items.ColorId;
import gw2.api.v2.items.ConsumableDetails;
import gw2.api.v2.items.ConsumableType;
import gw2.api.v2.items.ContainerDetails;
import gw2.api.v2.items.ContainerType;
import gw2.api.v2.items.DamageType;
import gw2.api.v2.items.DefaultSkin;
import gw2.api.v2.items.Defense;
import gw2.api.v2.items.Description;
import gw2.api.v2.items.Details;
import gw2.api.v2.items.DurationMs;
import gw2.api.v2.items.Flag;
import gw2.api.v2.items.GameType;
import gw2.api.v2.items.GatheringToolDetails;
import gw2.api.v2.items.GatheringToolType;
import gw2.api.v2.items.GizmoDetails;
import gw2.api.v2.items.GizmoType;
import gw2.api.v2.items.Icon;
import gw2.api.v2.items.Id;
import gw2.api.v2.items.InfixUpgrade;
import gw2.api.v2.items.InfusionSlot;
import gw2.api.v2.items.InfusionSlotFlag;
import gw2.api.v2.items.InfusionSlotItemId;
import gw2.api.v2.items.InfusionUpgradeFlag;
import gw2.api.v2.items.Item;
import gw2.api.v2.items.ItemType;
import gw2.api.v2.items.Level;
import gw2.api.v2.items.MaxPower;
import gw2.api.v2.items.MinPower;
import gw2.api.v2.items.MiniatureDetails;
import gw2.api.v2.items.MinipetId;
import gw2.api.v2.items.Modifier;
import gw2.api.v2.items.Name;
import gw2.api.v2.items.NoSellOrSort;
import gw2.api.v2.items.Price;
import gw2.api.v2.items.Rarity;
import gw2.api.v2.items.RecipeId;
import gw2.api.v2.items.Restriction;
import gw2.api.v2.items.SalvageKitDetails;
import gw2.api.v2.items.SalvageKitType;
import gw2.api.v2.items.SecondarySuffixItemId;
import gw2.api.v2.items.Size;
import gw2.api.v2.items.SkillId;
import gw2.api.v2.items.StatChoice;
import gw2.api.v2.items.Suffix;
import gw2.api.v2.items.SuffixItemId;
import gw2.api.v2.items.TrinketDetails;
import gw2.api.v2.items.TrinketType;
import gw2.api.v2.items.UnlockType;
import gw2.api.v2.items.UpgradeComponentDetails;
import gw2.api.v2.items.UpgradeComponentFlag;
import gw2.api.v2.items.UpgradeComponentType;
import gw2.api.v2.items.WeaponDetails;
import gw2.api.v2.items.WeaponType;
import gw2.api.v2.items.WeightClass;
import gw2.api.v2.listings.Listing;
import gw2.api.v2.listings.ListingItem;
import gw2.api.v2.recipes.Count;
import gw2.api.v2.recipes.CraftingDiscipline;
import gw2.api.v2.recipes.GuildIngredient;
import gw2.api.v2.recipes.Ingredient;
import gw2.api.v2.recipes.MinRating;
import gw2.api.v2.recipes.Recipe;
import gw2.api.v2.recipes.RecipeFlag;
import gw2.api.v2.recipes.RecipeType;
import junit.framework.TestCase;

public class Gw2Test extends TestCase {
	@JsonDeserialize(builder = Listing.Builder.class)
	interface ListingMixIn {
	}
	@JsonDeserialize(builder = ListingItem.Builder.class)
	interface ListingItemMixIn {
	}
	@JsonDeserialize(builder = GuildIngredient.Builder.class)
	interface GuildIngredientMixIn {
	}
	@JsonDeserialize(builder = Ingredient.Builder.class)
	interface IngredientMixIn {
	}
	@JsonDeserialize(builder = Recipe.Builder.class)
	interface RecipeMixIn {
	}
	@JsonDeserialize(builder = ArmorDetails.Builder.class)
	interface ArmorDetailsMixIn {
	}

	@JsonDeserialize(builder = AttributeBonus.Builder.class)
	interface AttributeBonusMixIn {
	}

	@JsonDeserialize(builder = BackDetails.Builder.class)
	interface BackDetailsMixIn {
	}

	@JsonDeserialize(builder = BagDetails.Builder.class)
	interface BagDetailsMixIn {
	}

	@JsonDeserialize(builder = Buff.Builder.class)
	interface BuffMixIn {
	}

	@JsonDeserialize(builder = ConsumableDetails.Builder.class)
	interface ConsumableDetailsMixIn {
	}

	@JsonDeserialize(builder = ContainerDetails.Builder.class)
	interface ContainerDetailsMixIn {
	}

	@JsonDeserialize(builder = GatheringToolDetails.Builder.class)
	interface GatheringToolDetailsMixIn {
	}

	@JsonDeserialize(builder = GizmoDetails.Builder.class)
	interface GizmoDetailsMixIn {
	}

	@JsonDeserialize(builder = InfixUpgrade.Builder.class)
	interface InfixUpgradeMixIn {
	}

	@JsonDeserialize(builder = InfusionSlot.Builder.class)
	interface InfusionSlotMixIn {
	}
	
	@JsonDeserialize(builder = Item.Builder.class)
	interface ItemMixIn {
	}
	@JsonDeserialize(builder = Item.Builder.class)
	abstract class ItemBuilderMixIn {
		@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
	            include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
	            property = "type",
	            visible = true
	            )
		@JsonSubTypes(value = { 
				@JsonSubTypes.Type(value = ArmorDetails.class, name = "Armor"),
		        @JsonSubTypes.Type(value = BackDetails.class, name = "Back"),
		        @JsonSubTypes.Type(value = BagDetails.class, name = "Bag"),
		        @JsonSubTypes.Type(value = ConsumableDetails.class, name = "Consumable"),
		        @JsonSubTypes.Type(value = ContainerDetails.class, name = "Container"),
		        @JsonSubTypes.Type(value = GatheringToolDetails.class, name = "Gathering"),
		        @JsonSubTypes.Type(value = GizmoDetails.class, name = "Gizmo"),
		        @JsonSubTypes.Type(value = MiniatureDetails.class, name = "MiniPet"),
		        @JsonSubTypes.Type(value = SalvageKitDetails.class, name = "Tool"),
		        @JsonSubTypes.Type(value = TrinketDetails.class, name = "Trinket"),
		        @JsonSubTypes.Type(value = UpgradeComponentDetails.class, name = "UpgradeComponent"),
		        @JsonSubTypes.Type(value = WeaponDetails.class, name = "Weapon"),
		        })
		public Details details;
	}

	@JsonDeserialize(builder = MiniatureDetails.Builder.class)
	interface MiniatureDetailsMixIn {
	}

	@JsonDeserialize(builder = SalvageKitDetails.Builder.class)
	interface SalvageKitDetailsMixIn {
	}

	@JsonDeserialize(builder = TrinketDetails.Builder.class)
	interface TrinketDetailsMixIn {
	}

	@JsonDeserialize(builder = UpgradeComponentDetails.Builder.class)
	interface UpgradeComponentDetailsMixIn {
	}

	@JsonDeserialize(builder = WeaponDetails.Builder.class)
	interface WeaponDetailsMixIn {
	}

	interface EnumValueMixIn {
		@JsonValue
		public String getValue();

		@JsonCreator
		public void from();
	}

	interface GetValueMixIn {
		@JsonValue
		public void getValue();
	}
	ObjectMapper mapper;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
		mapper.disable(DeserializationFeature.FAIL_ON_MISSING_EXTERNAL_TYPE_ID_PROPERTY);
		mapper.addMixIn(ArmorSlotType.class, EnumValueMixIn.class);
		mapper.addMixIn(Attribute.class, EnumValueMixIn.class);
		mapper.addMixIn(ConsumableType.class, EnumValueMixIn.class);
		mapper.addMixIn(ContainerType.class, EnumValueMixIn.class);
		mapper.addMixIn(DamageType.class, EnumValueMixIn.class);
		mapper.addMixIn(Flag.class, EnumValueMixIn.class);
		mapper.addMixIn(GameType.class, EnumValueMixIn.class);
		mapper.addMixIn(GatheringToolType.class, EnumValueMixIn.class);
		mapper.addMixIn(GizmoType.class, EnumValueMixIn.class);
		mapper.addMixIn(InfusionSlotFlag.class, EnumValueMixIn.class);
		mapper.addMixIn(InfusionUpgradeFlag.class, EnumValueMixIn.class);
		mapper.addMixIn(ItemType.class, EnumValueMixIn.class);
		mapper.addMixIn(Rarity.class, EnumValueMixIn.class);
		mapper.addMixIn(Restriction.class, EnumValueMixIn.class);
		mapper.addMixIn(SalvageKitType.class, EnumValueMixIn.class);
		mapper.addMixIn(TrinketType.class, EnumValueMixIn.class);
		mapper.addMixIn(UnlockType.class, EnumValueMixIn.class);
		mapper.addMixIn(UpgradeComponentFlag.class, EnumValueMixIn.class);
		mapper.addMixIn(UpgradeComponentType.class, EnumValueMixIn.class);
		mapper.addMixIn(WeaponType.class, EnumValueMixIn.class);
		mapper.addMixIn(WeightClass.class, EnumValueMixIn.class);
		mapper.addMixIn(ApplyCount.class, GetValueMixIn.class);
		mapper.addMixIn(Bonus.class, GetValueMixIn.class);
		mapper.addMixIn(Charges.class, GetValueMixIn.class);
		mapper.addMixIn(ChatLink.class, GetValueMixIn.class);
		mapper.addMixIn(ColorId.class, GetValueMixIn.class);
		mapper.addMixIn(DefaultSkin.class, GetValueMixIn.class);
		mapper.addMixIn(Defense.class, GetValueMixIn.class);
		mapper.addMixIn(Description.class, GetValueMixIn.class);
		mapper.addMixIn(DurationMs.class, GetValueMixIn.class);
		mapper.addMixIn(Icon.class, GetValueMixIn.class);
		mapper.addMixIn(Id.class, GetValueMixIn.class);
		mapper.addMixIn(InfusionSlotItemId.class, GetValueMixIn.class);
		mapper.addMixIn(Level.class, GetValueMixIn.class);
		mapper.addMixIn(MaxPower.class, GetValueMixIn.class);
		mapper.addMixIn(MinipetId.class, GetValueMixIn.class);
		mapper.addMixIn(MinPower.class, GetValueMixIn.class);
		mapper.addMixIn(Modifier.class, GetValueMixIn.class);
		mapper.addMixIn(Name.class, GetValueMixIn.class);
		mapper.addMixIn(NoSellOrSort.class, GetValueMixIn.class);
		mapper.addMixIn(RecipeId.class, GetValueMixIn.class);
		mapper.addMixIn(SecondarySuffixItemId.class, GetValueMixIn.class);
		mapper.addMixIn(Size.class, GetValueMixIn.class);
		mapper.addMixIn(SkillId.class, GetValueMixIn.class);
		mapper.addMixIn(StatChoice.class, GetValueMixIn.class);
		mapper.addMixIn(Suffix.class, GetValueMixIn.class);
		mapper.addMixIn(SuffixItemId.class, GetValueMixIn.class);
		mapper.addMixIn(Price.class, GetValueMixIn.class);
		mapper.addMixIn(ArmorDetails.class, ArmorDetailsMixIn.class);
		mapper.addMixIn(AttributeBonus.class, AttributeBonusMixIn.class);
		mapper.addMixIn(BackDetails.class, BackDetailsMixIn.class);
		mapper.addMixIn(BagDetails.class, BagDetailsMixIn.class);
		mapper.addMixIn(Buff.class, BuffMixIn.class);
		mapper.addMixIn(ConsumableDetails.class, ConsumableDetailsMixIn.class);
		mapper.addMixIn(ContainerDetails.class, ContainerDetailsMixIn.class);
		mapper.addMixIn(GatheringToolDetails.class, GatheringToolDetailsMixIn.class);
		mapper.addMixIn(GizmoDetails.class, GizmoDetailsMixIn.class);
		mapper.addMixIn(InfixUpgrade.class, InfixUpgradeMixIn.class);
		mapper.addMixIn(InfusionSlot.class, InfusionSlotMixIn.class);
		mapper.addMixIn(Item.class, ItemMixIn.class);
		mapper.addMixIn(MiniatureDetails.class, MiniatureDetailsMixIn.class);
		mapper.addMixIn(SalvageKitDetails.class, SalvageKitDetailsMixIn.class);
		mapper.addMixIn(TrinketDetails.class, TrinketDetailsMixIn.class);
		mapper.addMixIn(UpgradeComponentDetails.class, UpgradeComponentDetailsMixIn.class);
		mapper.addMixIn(WeaponDetails.class, WeaponDetailsMixIn.class);
		mapper.addMixIn(Item.Builder.class, ItemBuilderMixIn.class);
		mapper.addMixIn(CraftingDiscipline.class, EnumValueMixIn.class);
		mapper.addMixIn(RecipeFlag.class, EnumValueMixIn.class);
		mapper.addMixIn(RecipeType.class, EnumValueMixIn.class);
		mapper.addMixIn(Count.class, GetValueMixIn.class);
		mapper.addMixIn(Id.class, GetValueMixIn.class);
		mapper.addMixIn(MinRating.class, GetValueMixIn.class);
		mapper.addMixIn(Suffix.class, GetValueMixIn.class);
		mapper.addMixIn(GuildIngredient.class, GuildIngredientMixIn.class);
		mapper.addMixIn(Ingredient.class, IngredientMixIn.class);
		mapper.addMixIn(Recipe.class, RecipeMixIn.class);
		mapper.addMixIn(Listing.class, ListingMixIn.class);
		mapper.addMixIn(ListingItem.class, ListingItemMixIn.class);
		driver = GraphDatabase.driver("bolt://localhost:7687",
				AuthTokens.basic("neo4j", "dt9LfmJAEmzKh9VZu6jD"));
		session = driver.session();
	}
	Driver driver;
	Session session;
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		session.close();
	    driver.close();
	}
	
	public void testConvertItems() throws JsonParseException, JsonMappingException, IOException, InterruptedException {
		FileInputStream fis = new FileInputStream(Paths.get("./items.json").toFile());
		Item[] myObjects = mapper.readValue(fis, Item[].class);
		System.out.println(myObjects.length);
	}
	public void testConvertRecipes() throws JsonParseException, JsonMappingException, IOException, InterruptedException {
		FileInputStream fis = new FileInputStream(Paths.get("./recipes.json").toFile());
		Recipe[] myObjects = mapper.readValue(fis, Recipe[].class);
		System.out.println(myObjects.length);
	}
	public void testConvertListings() throws JsonParseException, JsonMappingException, IOException, InterruptedException {
		FileInputStream fis = new FileInputStream(Paths.get("./listings.json").toFile());
		ListingItem[] myObjects = mapper.readValue(fis, ListingItem[].class);
		System.out.println(myObjects.length);
	}
	public void testInsertItemsIntoNeo4J() throws Exception{
		FileInputStream fis = new FileInputStream(Paths.get("./items.json").toFile());
		Item[] myObjects = mapper.readValue(fis, Item[].class);
		String mergeItem = "UNWIND $data AS item MERGE (i:Item { id: item.id}) ON CREATE SET i.chat_link=item.chat_link, i.name=item.name, i.icon=item.icon, i.description=item.description, i.level=    item.level, i.vendor_value=item.vendor_value, i.default_skin=item.default_skin "
				+ " MERGE (t:ItemType { name: item.type }) "
				+ " MERGE (i)-[:TYPE]->(t) "
				+ " MERGE (r:ItemRarity {name: item.rarity}) "
				+ " MERGE (i)-[:RARITY]->(r) "
				+ " FOREACH (flag in item.flags | MERGE (f:ItemFlag {name:flag}) MERGE (i)-[:HAS_FLAG]->(f))"
				+ " FOREACH (game_type in item.game_types | MERGE (g:ItemGameType {name:game_type}) MERGE (i)-[:USABLE_IN]->(g))"
				+ " FOREACH (restriction in item.restrictions| MERGE (rs:ItemRestriction {name:restriction}) MERGE (i)-[:RESTRICTED_TO]->(rs))";
		;
		
		long begin = System.currentTimeMillis();
		try (Transaction tx = session.beginTransaction()) {
			int count = 0;
			for (Item item : myObjects) {
				if(count++%1000 == 0 && count > 0 ){
					System.out.println(count++);
					tx.success();
				}
					tx.run(mergeItem,
							Values.parameters("data",mapper.convertValue(item, Map.class))); 
			}
			tx.success();
		}
		long end = System.currentTimeMillis();
		System.out.print(MessageFormat.format("Inserted {0} nodes in {1} ms.", myObjects.length, end - begin));
	}
	
	public void testInsertRecipesIntoNeo4J() throws Exception{
		FileInputStream fis = new FileInputStream(Paths.get("./items.json").toFile());
		Recipe[] myObjects = mapper.readValue(fis, Recipe[].class);
		String mergeItem = "UNWIND $data AS recipe MERGE (r:Recipe{ id: recipe.id}) "
				+ "ON CREATE SET r.chat_link=r.chat_link, r.min_rating=r.min_rating"
				+ " MERGE (t:RecipeType { name: recipe.type }) "
				+ " MERGE (r)-[:TYPE]->(t) "
				+ " MERGE (i:Item {id: recipe.output_item_id}) "
				+ " MERGE (r)-[:PRODUCES {count: recipe.output_item_count, duration: recipe.time_to_craft_ms }]->(i) "
				+ " FOREACH (discipline in recipe.disciplines | MERGE (d:Discipline {name:discipline}) MERGE (r)-[:USED_BY]->(d))"
				+ " FOREACH (flag in recipe.flags | MERGE (f:Flag {name:flag}) MERGE (r)-[:HAS_FLAG]->(f))"
				+ " FOREACH (ingredient in recipe.ingredients| MERGE (i:Item {id:ingredient.item_id}) MERGE (r)-[:REQUIRES {count: ingredient.count}]->(i))"
				+ " FOREACH (gingredient in recipe.guild_ingredients| MERGE (gi:GuildUpgrade {id:gingredient.item_id}) MERGE (r)-[:REQUIRES {count: gingredient.count}]->(gi))"		
		;
		
		long begin = System.currentTimeMillis();
		try (Transaction tx = session.beginTransaction()) {
			for (Recipe item : myObjects) {
					tx.run(mergeItem,
							Values.parameters("data",mapper.convertValue(item, Map.class))); 
			}
			tx.success();
		}
		long end = System.currentTimeMillis();
		System.out.print(MessageFormat.format("Inserted {0} nodes in {1} ms.", myObjects.length, end - begin));
	}
	
	public void testInsertListingsIntoNeo4J() throws Exception{
		FileInputStream fis = new FileInputStream(Paths.get("./items.json").toFile());
		Listing[] myObjects = mapper.readValue(fis, Listing[].class);
		String mergeItem = "UNWIND $data AS recipe MERGE (r:Recipe{ id: recipe.id}) "
				+ "ON CREATE SET r.chat_link=r.chat_link, r.min_rating=r.min_rating"
				+ " MERGE (t:RecipeType { name: recipe.type }) "
				+ " MERGE (r)-[:TYPE]->(t) "
				+ " MERGE (i:Item {id: recipe.output_item_id}) "
				+ " MERGE (r)-[:PRODUCES {count: recipe.output_item_count, duration: recipe.time_to_craft_ms }]->(i) "
				+ " FOREACH (discipline in recipe.disciplines | MERGE (d:Discipline {name:discipline}) MERGE (r)-[:USED_BY]->(d))"
				+ " FOREACH (flag in recipe.flags | MERGE (f:Flag {name:flag}) MERGE (r)-[:HAS_FLAG]->(f))"
				+ " FOREACH (ingredient in recipe.ingredients| MERGE (i:Item {id:ingredient.item_id}) MERGE (r)-[:REQUIRES {count: ingredient.count}]->(i))"
				+ " FOREACH (gingredient in recipe.guild_ingredients| MERGE (gi:GuildUpgrade {id:gingredient.item_id}) MERGE (r)-[:REQUIRES {count: gingredient.count}]->(gi))"		
		;
		
		long begin = System.currentTimeMillis();
		try (Transaction tx = session.beginTransaction()) {
			for (Listing item : myObjects) {
					tx.run(mergeItem,
							Values.parameters("data",mapper.convertValue(item, Map.class))); 
			}
			tx.success();
		}
		long end = System.currentTimeMillis();
		System.out.print(MessageFormat.format("Inserted {0} nodes in {1} ms.", myObjects.length, end - begin));
	}
	
	
	public void testGenerateSchema() throws JsonParseException, JsonMappingException, IOException, InterruptedException {
		JsonSchemaGenerator schemaGen = new JsonSchemaGenerator(mapper);
		List<Class<? extends Object>> asList = Arrays.asList(Item.class, ArmorDetails.class, BackDetails.class, BagDetails.class, ConsumableDetails.class, ContainerDetails.class, GatheringToolDetails.class, GizmoDetails.class, MiniatureDetails.class, SalvageKitDetails.class, TrinketDetails.class, UpgradeComponentDetails.class, WeaponDetails.class, Recipe.class, Listing.class );
		JsonSchema schema = null;
		for (Class<? extends Object> class1 : asList) {
			schema = schemaGen.generateSchema(class1);
			System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(schema));
		}
	}

}
