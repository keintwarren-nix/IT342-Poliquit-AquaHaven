package edu.cit.poliquit.aquahaven.config;

import edu.cit.poliquit.aquahaven.entity.Category;
import edu.cit.poliquit.aquahaven.entity.Product;
import edu.cit.poliquit.aquahaven.repository.CategoryRepository;
import edu.cit.poliquit.aquahaven.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final CategoryRepository categoryRepo;
    private final ProductRepository  productRepo;

    public DataSeeder(CategoryRepository categoryRepo, ProductRepository productRepo) {
        this.categoryRepo = categoryRepo;
        this.productRepo  = productRepo;
    }

    @Override
    public void run(String... args) {
        if (categoryRepo.count() > 0) return; // already seeded

        // ── Categories ────────────────────────────────────────────────────────
        Category fish  = cat("Freshwater Fish",  "freshwater-fish",  "🐠", 1);
        Category salt  = cat("Saltwater Fish",   "saltwater-fish",   "🐡", 2);
        Category plant = cat("Aquatic Plants",   "aquatic-plants",   "🌿", 3);
        Category food  = cat("Fish Food",        "fish-food",        "🫙", 4);
        Category equip = cat("Equipment",        "equipment",        "⚙️", 5);
        Category coral = cat("Coral & Marine",   "coral-marine",     "🪸", 6);
        Category deco  = cat("Decorations",      "decorations",      "🪨", 7);

        categoryRepo.saveAll(List.of(fish, salt, plant, food, equip, coral, deco));

        List<Product> products = new ArrayList<>();

        // ── FRESHWATER FISH (10) ──────────────────────────────────────────────
        products.add(prod("Guppy (Fancy Tail)",
                "One of the most popular aquarium fish in the Philippines. Hardy, colorful, and perfect for beginners.",
                new BigDecimal("45.00"), fish, "freshwater", 120,
                "https://upload.wikimedia.org/wikipedia/commons/thumb/0/00/Fancy_guppy_pair.jpg/320px-Fancy_guppy_pair.jpg"));

        products.add(prod("Goldfish (Oranda)",
                "Classic ornamental goldfish with a distinctive head growth (wen). Thrives in well-oxygenated tanks.",
                new BigDecimal("180.00"), fish, "freshwater", 60,
                "https://upload.wikimedia.org/wikipedia/commons/thumb/4/4c/Orange_oranda_01.jpg/320px-Orange_oranda_01.jpg"));

        products.add(prod("Neon Tetra",
                "Brilliant blue and red schooling fish. Best kept in groups of 6 or more. A staple in planted tanks.",
                new BigDecimal("35.00"), fish, "freshwater", 200,
                "https://upload.wikimedia.org/wikipedia/commons/thumb/6/67/NeonTetra.jpg/320px-NeonTetra.jpg"));

        products.add(prod("Betta Fish (Halfmoon)",
                "Stunning halfmoon betta with vibrant flowing fins. Keep singly — males are territorial.",
                new BigDecimal("250.00"), fish, "freshwater", 30,
                "https://upload.wikimedia.org/wikipedia/commons/thumb/5/54/Betta_fish_01.jpg/320px-Betta_fish_01.jpg"));

        products.add(prod("Molly (Black)",
                "Peaceful community fish. Easy to breed and adapts to a wide range of water conditions.",
                new BigDecimal("40.00"), fish, "freshwater", 90,
                "https://upload.wikimedia.org/wikipedia/commons/thumb/d/dc/Poecilia_sphenops.jpg/320px-Poecilia_sphenops.jpg"));

        products.add(prod("Pleco (Common)",
                "Algae-eating bottom dweller. Excellent tank cleaner; grows up to 50 cm — needs a large tank.",
                new BigDecimal("95.00"), fish, "freshwater", 45,
                "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6d/Hypostomus_plecostomus.jpg/320px-Hypostomus_plecostomus.jpg"));

        products.add(prod("Corydoras Catfish",
                "Bottom-feeding schooling fish. Keeps substrate clean. Best in groups of 4+.",
                new BigDecimal("75.00"), fish, "freshwater", 70,
                "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a3/Corydoras.jpg/320px-Corydoras.jpg"));

        products.add(prod("Koi (Standard Grade)",
                "Traditional Japanese pond fish popular in Philippine garden ponds and large display tanks.",
                new BigDecimal("450.00"), fish, "freshwater", 20,
                "https://upload.wikimedia.org/wikipedia/commons/thumb/2/2e/Koi_fish.jpg/320px-Koi_fish.jpg"));

        products.add(prod("Angelfish (Marble)",
                "Elegant cichlid with marble pattern. Semi-aggressive; avoid housing with small neon tetras.",
                new BigDecimal("160.00"), fish, "freshwater", 35,
                "https://upload.wikimedia.org/wikipedia/commons/thumb/e/ef/Pterophyllum_scalare.jpg/320px-Pterophyllum_scalare.jpg"));

        products.add(prod("Swordtail (Red)",
                "Active and hardy livebearer. The bright red color and sword-shaped tail make them a showpiece.",
                new BigDecimal("55.00"), fish, "freshwater", 80,
                "https://upload.wikimedia.org/wikipedia/commons/thumb/c/c9/Xiphophorus_hellerii.jpg/320px-Xiphophorus_hellerii.jpg"));

        products.add(prod("Discus (Blue Diamond)",
                "The 'King of the Aquarium'. Requires warm, soft water and pristine conditions but rewards with unmatched beauty.",
                new BigDecimal("1200.00"), fish, "freshwater", 8,
                "https://upload.wikimedia.org/wikipedia/commons/thumb/5/57/Discus_fish.jpg/320px-Discus_fish.jpg"));

        products.add(prod("Platy (Sunset)",
                "Colorful and peaceful community fish. Easy for beginners; compatible with guppies and mollies.",
                new BigDecimal("38.00"), fish, "freshwater", 110,
                "https://upload.wikimedia.org/wikipedia/commons/thumb/2/22/Xiphophorus_maculatus.jpg/320px-Xiphophorus_maculatus.jpg"));

        // ── SALTWATER FISH (10) ───────────────────────────────────────────────
        products.add(prod("Clownfish (Ocellaris)",
                "The iconic 'Nemo' fish. Hardy for a reef tank; pairs well with host anemones.",
                new BigDecimal("650.00"), salt, "saltwater", 25,
                "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ad/Clown_fish_in_Pacific_coral_reef.jpg/320px-Clown_fish_in_Pacific_coral_reef.jpg"));

        products.add(prod("Blue Tang",
                "Vivid royal-blue tang beloved by reef hobbyists. Requires a minimum 300-liter tank.",
                new BigDecimal("1200.00"), salt, "saltwater", 10,
                "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b7/Blue_tang.jpg/320px-Blue_tang.jpg"));

        products.add(prod("Yellow Tang",
                "Bright yellow reef fish. Active algae grazer that helps control nuisance algae in marine tanks.",
                new BigDecimal("850.00"), salt, "saltwater", 12,
                "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/Yellow_Tang.jpg/320px-Yellow_Tang.jpg"));

        products.add(prod("Firefish Goby",
                "Slender, colorful goby with a fiery orange-red gradient. Peaceful; great for nano reef setups.",
                new BigDecimal("480.00"), salt, "saltwater", 18,
                "https://upload.wikimedia.org/wikipedia/commons/thumb/3/30/Nemateleotris_magnifica.jpg/320px-Nemateleotris_magnifica.jpg"));

        products.add(prod("Mandarin Dragonet",
                "One of the most colorful fish in the hobby. Psychedelic blue-green pattern. Requires a mature reef with copepods.",
                new BigDecimal("980.00"), salt, "saltwater", 6,
                "https://upload.wikimedia.org/wikipedia/commons/thumb/5/5c/Synchiropus_splendidus_2_Luc_Viatour.jpg/320px-Synchiropus_splendidus_2_Luc_Viatour.jpg"));

        products.add(prod("Royal Gramma",
                "Bi-colored fish — bold purple front, vivid yellow tail. Hardy reef dweller. Great for community marine tanks.",
                new BigDecimal("720.00"), salt, "saltwater", 14,
                "https://upload.wikimedia.org/wikipedia/commons/thumb/7/74/Royal_Gramma.jpg/320px-Royal_Gramma.jpg"));

        products.add(prod("Coral Beauty Angelfish",
                "Dwarf angelfish with stunning orange and blue coloration. Can nip at coral polyps — monitor carefully.",
                new BigDecimal("950.00"), salt, "saltwater", 9,
                "https://upload.wikimedia.org/wikipedia/commons/thumb/c/c5/Centropyge_bispinosa.jpg/320px-Centropyge_bispinosa.jpg"));

        products.add(prod("Banggai Cardinalfish",
                "Striking black-and-silver striped cardinal from the Banggai Islands. Peaceful and reef-safe.",
                new BigDecimal("550.00"), salt, "saltwater", 16,
                "https://upload.wikimedia.org/wikipedia/commons/thumb/b/ba/Banggai_cardinalfish.jpg/320px-Banggai_cardinalfish.jpg"));

        products.add(prod("Six Line Wrasse",
                "Active and colorful wrasse with six bold horizontal stripes. Reef-safe; eats small bristle worms.",
                new BigDecimal("680.00"), salt, "saltwater", 11,
                "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e2/Pseudocheilinus_hexataenia.jpg/320px-Pseudocheilinus_hexataenia.jpg"));

        products.add(prod("Blenny (Tailspot)",
                "Perky bottom-sitting fish with big eyes and a distinctive tail spot. Algae grazer; reef-compatible.",
                new BigDecimal("420.00"), salt, "saltwater", 20,
                "https://upload.wikimedia.org/wikipedia/commons/thumb/5/56/Ecsenius_stigmatura.jpg/320px-Ecsenius_stigmatura.jpg"));

        products.add(prod("Pajama Cardinalfish",
                "Polka-dotted cardinal with red and yellow markings. Slow-moving, peaceful, and reef-safe.",
                new BigDecimal("490.00"), salt, "saltwater", 15,
                "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a1/Sphaeramia_nematoptera.jpg/320px-Sphaeramia_nematoptera.jpg"));

        // ── AQUATIC PLANTS (10) ───────────────────────────────────────────────
        products.add(prod("Java Fern (Microsorum pteropus)",
                "The most beginner-friendly aquatic plant. Attach to driftwood or rocks — no substrate planting needed.",
                new BigDecimal("90.00"), plant, null, 150,
                "https://upload.wikimedia.org/wikipedia/commons/thumb/8/87/Java_Fern.jpg/320px-Java_Fern.jpg"));

        products.add(prod("Anubias Barteri",
                "Slow-growing, extremely tough plant that thrives in low light. Ideal for community and cichlid tanks.",
                new BigDecimal("120.00"), plant, null, 100,
                "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7f/Anubias_barteri.jpg/320px-Anubias_barteri.jpg"));

        products.add(prod("Amazon Sword (Echinodorus)",
                "Lush background plant that fills space beautifully. Requires nutrient-rich substrate and moderate light.",
                new BigDecimal("85.00"), plant, null, 80,
                "https://upload.wikimedia.org/wikipedia/commons/thumb/f/f1/Amazon_sword_plant.jpg/320px-Amazon_sword_plant.jpg"));

        products.add(prod("Hornwort (Ceratophyllum)",
                "Fast-growing floating plant. Excellent natural water purifier and fry hiding spot.",
                new BigDecimal("60.00"), plant, null, 200,
                "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b2/Ceratophyllum_demersum.jpg/320px-Ceratophyllum_demersum.jpg"));

        products.add(prod("Water Sprite (Ceratopteris)",
                "Feathery plant great for surface cover. Locally available, very affordable, and grows quickly.",
                new BigDecimal("40.00"), plant, null, 180,
                "https://upload.wikimedia.org/wikipedia/commons/thumb/9/96/Ceratopteris_thalictroides.jpg/320px-Ceratopteris_thalictroides.jpg"));

        products.add(prod("Dwarf Sagittaria",
                "Grass-like foreground plant that spreads quickly through runners. Great for a natural carpet effect.",
                new BigDecimal("75.00"), plant, null, 110,
                "https://upload.wikimedia.org/wikipedia/commons/thumb/8/85/Sagittaria_subulata.jpg/320px-Sagittaria_subulata.jpg"));

        products.add(prod("Cryptocoryne Wendtii (Brown)",
                "Slow-growing Crypt that adds rich brown-green texture to the midground. Low light, low fuss.",
                new BigDecimal("95.00"), plant, null, 90,
                "https://upload.wikimedia.org/wikipedia/commons/thumb/2/29/Cryptocoryne_wendtii.jpg/320px-Cryptocoryne_wendtii.jpg"));

        products.add(prod("Water Wisteria (Hygrophila difformis)",
                "Fast-growing stem plant with delicate, feathery leaves. Great for beginners; helps control algae.",
                new BigDecimal("55.00"), plant, null, 140,
                "https://upload.wikimedia.org/wikipedia/commons/thumb/5/50/Hygrophila_difformis.jpg/320px-Hygrophila_difformis.jpg"));

        products.add(prod("Rotala Rotundifolia",
                "Reddish-pink stem plant that adds vivid color contrast in planted aquascapes. Needs moderate to high light.",
                new BigDecimal("70.00"), plant, null, 120,
                "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7f/Rotala_rotundifolia.jpg/320px-Rotala_rotundifolia.jpg"));

        products.add(prod("Ludwigia Repens",
                "Popular red-leaved stem plant for Dutch and nature aquascapes. Intensifies in color under high light.",
                new BigDecimal("80.00"), plant, null, 100,
                "https://upload.wikimedia.org/wikipedia/commons/thumb/3/33/Ludwigia_repens.jpg/320px-Ludwigia_repens.jpg"));

        products.add(prod("Bucephalandra (Mini Coin)",
                "Rare and sought-after plant from Borneo. Jewel-like leaves with iridescent shimmer. Attach to hardscape.",
                new BigDecimal("185.00"), plant, null, 40,
                "https://upload.wikimedia.org/wikipedia/commons/thumb/2/27/Bucephalandra.jpg/320px-Bucephalandra.jpg"));

        // ── FISH FOOD (10) ────────────────────────────────────────────────────
        products.add(prod("Tetra TetraMin Flakes (100g)",
                "Premium tropical fish flake food. High protein formula for vibrant color and healthy growth.",
                new BigDecimal("195.00"), food, null, 300,
                "https://images.unsplash.com/photo-1574781330855-d0db8cc6a79c?w=320&q=80"));

        products.add(prod("Hikari Micro Pellets (45g)",
                "Micro-sized sinking pellets ideal for small-mouthed fish like corydoras and small tetras.",
                new BigDecimal("220.00"), food, null, 250,
                "https://images.unsplash.com/photo-1574781330855-d0db8cc6a79c?w=320&q=80"));

        products.add(prod("Betta Pellets — Omega One (28g)",
                "Specially formulated floating pellets for bettas. Salmon-based for rich color enhancement.",
                new BigDecimal("175.00"), food, null, 200,
                "https://images.unsplash.com/photo-1574781330855-d0db8cc6a79c?w=320&q=80"));

        products.add(prod("Freeze-Dried Bloodworms (12g)",
                "High-protein treat for most tropical fish. Great for conditioning bettas and angelfish pre-breeding.",
                new BigDecimal("145.00"), food, null, 180,
                "https://images.unsplash.com/photo-1574781330855-d0db8cc6a79c?w=320&q=80"));

        products.add(prod("Spirulina Flakes (50g)",
                "Plant-based flake food ideal for herbivorous fish like mollies, platies, and tangs.",
                new BigDecimal("160.00"), food, null, 220,
                "https://images.unsplash.com/photo-1574781330855-d0db8cc6a79c?w=320&q=80"));

        products.add(prod("Koi & Goldfish Pellets (200g)",
                "Floating pellets formulated for koi and goldfish. Enhances color and boosts immune health.",
                new BigDecimal("180.00"), food, null, 160,
                "https://images.unsplash.com/photo-1574781330855-d0db8cc6a79c?w=320&q=80"));

        products.add(prod("Hikari Discus Bio-Gold (80g)",
                "Specially formulated sinking pellets for discus. Rich in vitamins and minerals for optimal health.",
                new BigDecimal("380.00"), food, null, 80,
                "https://images.unsplash.com/photo-1574781330855-d0db8cc6a79c?w=320&q=80"));

        products.add(prod("Freeze-Dried Brine Shrimp (7g)",
                "Excellent supplemental food for freshwater and marine fish. Natural diet enrichment.",
                new BigDecimal("130.00"), food, null, 190,
                "https://images.unsplash.com/photo-1574781330855-d0db8cc6a79c?w=320&q=80"));

        products.add(prod("Marine Flakes — Ocean Nutrition (71g)",
                "Complete marine diet for saltwater fish. High palatability with natural color enhancers.",
                new BigDecimal("290.00"), food, null, 120,
                "https://images.unsplash.com/photo-1574781330855-d0db8cc6a79c?w=320&q=80"));

        products.add(prod("Vibra Bites Pellets — Hikari (35g)",
                "Worm-shaped sinking pellets that mimic live food. Excellent for picky eaters like loaches and catfish.",
                new BigDecimal("210.00"), food, null, 140,
                "https://images.unsplash.com/photo-1574781330855-d0db8cc6a79c?w=320&q=80"));

        products.add(prod("Nori Seaweed Sheets (10pcs)",
                "Clip-on algae sheets for tangs, rabbitfish, and other marine herbivores. 100% natural.",
                new BigDecimal("95.00"), food, null, 200,
                "https://images.unsplash.com/photo-1574781330855-d0db8cc6a79c?w=320&q=80"));

        // ── EQUIPMENT (10) ────────────────────────────────────────────────────
        products.add(prod("Hang-On-Back Filter (300 L/hr)",
                "Reliable HOB filter suitable for tanks up to 60 liters. Easy maintenance with replaceable cartridges.",
                new BigDecimal("650.00"), equip, null, 40,
                "https://images.unsplash.com/photo-1559827260-dc66d52bef19?w=320&q=80"));

        products.add(prod("Aquarium Air Pump (Double Outlet)",
                "Dual outlet air pump with adjustable flow. Quiet operation — perfect for Philippine humidity.",
                new BigDecimal("280.00"), equip, null, 75,
                "https://images.unsplash.com/photo-1559827260-dc66d52bef19?w=320&q=80"));

        products.add(prod("Submersible Heater 50W",
                "Preset at 26°C — ideal for tropical fish in Philippine climate. Shatterproof glass casing.",
                new BigDecimal("420.00"), equip, null, 55,
                "https://images.unsplash.com/photo-1559827260-dc66d52bef19?w=320&q=80"));

        products.add(prod("LED Aquarium Light (45–60 cm)",
                "Full-spectrum white and blue LEDs. Supports plant growth and enhances fish color.",
                new BigDecimal("850.00"), equip, null, 30,
                "https://images.unsplash.com/photo-1559827260-dc66d52bef19?w=320&q=80"));

        products.add(prod("Aquarium Water Test Kit (5-in-1)",
                "Tests pH, ammonia, nitrite, nitrate, and hardness. Essential for any new tank setup.",
                new BigDecimal("390.00"), equip, null, 60,
                "https://images.unsplash.com/photo-1559827260-dc66d52bef19?w=320&q=80"));

        products.add(prod("Gravel Vacuum / Siphon Cleaner",
                "Manual siphon for water changes and substrate cleaning. 45 cm hose with wide gravel tube.",
                new BigDecimal("195.00"), equip, null, 90,
                "https://images.unsplash.com/photo-1559827260-dc66d52bef19?w=320&q=80"));

        products.add(prod("Canister Filter (600 L/hr)",
                "Powerful external canister filter for tanks up to 150 liters. Ultra-quiet motor; includes media.",
                new BigDecimal("1850.00"), equip, null, 15,
                "https://images.unsplash.com/photo-1559827260-dc66d52bef19?w=320&q=80"));

        products.add(prod("Digital Aquarium Thermometer",
                "Accurate LCD thermometer with external probe. Keeps track of temperature without disturbing fish.",
                new BigDecimal("145.00"), equip, null, 100,
                "https://images.unsplash.com/photo-1559827260-dc66d52bef19?w=320&q=80"));

        products.add(prod("CO2 Diffuser (Glass)",
                "Borosilicate glass CO2 diffuser for planted tanks. Creates micro-bubbles for efficient CO2 absorption.",
                new BigDecimal("320.00"), equip, null, 35,
                "https://images.unsplash.com/photo-1559827260-dc66d52bef19?w=320&q=80"));

        products.add(prod("Aquarium Net (Fine Mesh, 15 cm)",
                "Soft fine-mesh net to safely catch and transfer delicate fish without injury.",
                new BigDecimal("75.00"), equip, null, 150,
                "https://images.unsplash.com/photo-1559827260-dc66d52bef19?w=320&q=80"));

        products.add(prod("Protein Skimmer (Nano 100L)",
                "Essential for reef and marine tanks. Removes organic waste before it breaks down into ammonia.",
                new BigDecimal("1250.00"), equip, null, 12,
                "https://images.unsplash.com/photo-1559827260-dc66d52bef19?w=320&q=80"));

        // ── CORAL & MARINE (10) ───────────────────────────────────────────────
        products.add(prod("Zoanthid Frag (Mixed Colors)",
                "Colorful polyp coral frag — beginner-friendly. Thrives under moderate reef lighting.",
                new BigDecimal("380.00"), coral, "saltwater", 20,
                "https://upload.wikimedia.org/wikipedia/commons/thumb/b/be/Zoanthids.jpg/320px-Zoanthids.jpg"));

        products.add(prod("Mushroom Coral (Discosoma)",
                "Soft coral that tolerates low flow and moderate light. Great starter coral for Filipino reef keepers.",
                new BigDecimal("290.00"), coral, "saltwater", 25,
                "https://upload.wikimedia.org/wikipedia/commons/thumb/2/22/Discosoma_sp.jpg/320px-Discosoma_sp.jpg"));

        products.add(prod("Hammer Coral (Euphyllia ancora)",
                "LPS coral with distinctive hammer-shaped polyps. Medium care level; stunning centerpiece for reef tanks.",
                new BigDecimal("890.00"), coral, "saltwater", 8,
                "https://upload.wikimedia.org/wikipedia/commons/thumb/7/70/Euphyllia_ancora.jpg/320px-Euphyllia_ancora.jpg"));

        products.add(prod("Torch Coral (Euphyllia glabrescens)",
                "Long flowing polyps that sway gently in current. Moderate care; keep away from aggressive corals.",
                new BigDecimal("950.00"), coral, "saltwater", 7,
                "https://upload.wikimedia.org/wikipedia/commons/thumb/5/5e/Euphyllia_glabrescens.jpg/320px-Euphyllia_glabrescens.jpg"));

        products.add(prod("Duncan Coral (Duncanopsammia axifuga)",
                "Branching LPS coral with large, puffy polyps. One of the easiest LPS corals for beginners.",
                new BigDecimal("650.00"), coral, "saltwater", 10,
                "https://upload.wikimedia.org/wikipedia/commons/thumb/9/9f/Duncan_coral.jpg/320px-Duncan_coral.jpg"));

        products.add(prod("Kenya Tree Coral",
                "Fast-growing soft coral that frags easily. Tolerant of various conditions — excellent starter coral.",
                new BigDecimal("220.00"), coral, "saltwater", 18,
                "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6c/Capnella_imbricata.jpg/320px-Capnella_imbricata.jpg"));

        products.add(prod("Toadstool Leather Coral",
                "Large, dome-shaped soft coral. Hardy, fast-growing, and very forgiving of less-than-perfect water.",
                new BigDecimal("480.00"), coral, "saltwater", 12,
                "https://upload.wikimedia.org/wikipedia/commons/thumb/8/89/Sarcophyton_coral.jpg/320px-Sarcophyton_coral.jpg"));

        products.add(prod("Bubble Tip Anemone (Rose)",
                "Host anemone for clownfish. Stunning pink-red color under reef lighting. Needs stable parameters.",
                new BigDecimal("1200.00"), coral, "saltwater", 5,
                "https://upload.wikimedia.org/wikipedia/commons/thumb/3/36/Entacmaea_quadricolor.jpg/320px-Entacmaea_quadricolor.jpg"));

        products.add(prod("Xenia (Pulsing Xenia)",
                "Unique pulsing soft coral. Grows fast and spreads — great for filling empty space on the rockwork.",
                new BigDecimal("310.00"), coral, "saltwater", 16,
                "https://upload.wikimedia.org/wikipedia/commons/thumb/4/4e/Xenia_coral.jpg/320px-Xenia_coral.jpg"));

        products.add(prod("Acropora Frag (Beginner Colony)",
                "SPS coral frag for intermediate reefers. Requires high flow, high light, and stable reef parameters.",
                new BigDecimal("1500.00"), coral, "saltwater", 4,
                "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d7/Acropora_coral.jpg/320px-Acropora_coral.jpg"));

        products.add(prod("Blue Leg Hermit Crab (5-pack)",
                "Essential reef cleanup crew. Eats algae, detritus, and uneaten food. Reef-safe and active.",
                new BigDecimal("180.00"), coral, "saltwater", 30,
                "https://upload.wikimedia.org/wikipedia/commons/thumb/3/30/Blue_leg_hermit_crab.jpg/320px-Blue_leg_hermit_crab.jpg"));

        // ── DECORATIONS (10) ─────────────────────────────────────────────────
        products.add(prod("Natural Driftwood (S size)",
                "Aquarium-safe Malaysian driftwood. Creates natural hiding spots and slightly lowers pH.",
                new BigDecimal("220.00"), deco, null, 50,
                "https://images.unsplash.com/photo-1519052537078-e6302a4968d4?w=320&q=80"));

        products.add(prod("Black Aquarium Gravel (2 kg)",
                "Smooth black substrate that makes fish colors pop. Pre-washed and ready to use.",
                new BigDecimal("150.00"), deco, null, 120,
                "https://images.unsplash.com/photo-1519052537078-e6302a4968d4?w=320&q=80"));

        products.add(prod("Coconut Cave Hideout",
                "Handcrafted from natural coconut shell — locally made in the Philippines! Safe, chemical-free fish shelter.",
                new BigDecimal("85.00"), deco, null, 80,
                "https://images.unsplash.com/photo-1519052537078-e6302a4968d4?w=320&q=80"));

        products.add(prod("Artificial Coral Ornament Set",
                "Set of 3 colorful artificial corals. Adds color without the maintenance of live coral.",
                new BigDecimal("195.00"), deco, null, 65,
                "https://images.unsplash.com/photo-1519052537078-e6302a4968d4?w=320&q=80"));

        products.add(prod("Lava Rock (1 kg)",
                "Porous volcanic rock — excellent surface area for beneficial bacteria. Great for natural aquascapes.",
                new BigDecimal("120.00"), deco, null, 100,
                "https://images.unsplash.com/photo-1519052537078-e6302a4968d4?w=320&q=80"));

        products.add(prod("Dragon Stone (Ohko Stone) 0.5 kg",
                "Intricate, weathered dragon stone popular in Iwagumi-style aquascapes. Neutral pH impact.",
                new BigDecimal("250.00"), deco, null, 45,
                "https://images.unsplash.com/photo-1519052537078-e6302a4968d4?w=320&q=80"));

        products.add(prod("Aquarium Background (Blue Gradient, 60 cm)",
                "Self-adhesive 3D-effect background. Hides cords and equipment while enhancing the aquascape depth.",
                new BigDecimal("130.00"), deco, null, 70,
                "https://images.unsplash.com/photo-1519052537078-e6302a4968d4?w=320&q=80"));

        products.add(prod("Sunken Shipwreck Ornament",
                "Classic pirate ship decoration. Resin-made, fish-safe. Great conversation piece for display tanks.",
                new BigDecimal("280.00"), deco, null, 35,
                "https://images.unsplash.com/photo-1519052537078-e6302a4968d4?w=320&q=80"));

        products.add(prod("Colored Aquarium Sand (White, 2 kg)",
                "Fine white sand substrate that brightens the tank floor. Safe for bottom-dwelling fish.",
                new BigDecimal("175.00"), deco, null, 90,
                "https://images.unsplash.com/photo-1519052537078-e6302a4968d4?w=320&q=80"));

        products.add(prod("Bamboo Air Curtain (30 cm)",
                "Decorative bamboo-style bubble wand. Creates a curtain of bubbles and oxygenates the water.",
                new BigDecimal("95.00"), deco, null, 60,
                "https://images.unsplash.com/photo-1519052537078-e6302a4968d4?w=320&q=80"));

        products.add(prod("Aquatic Figurine — Mermaid",
                "Resin mermaid figurine. Non-toxic and fish-safe. Adds a whimsical touch to any aquarium.",
                new BigDecimal("145.00"), deco, null, 55,
                "https://images.unsplash.com/photo-1519052537078-e6302a4968d4?w=320&q=80"));

        productRepo.saveAll(products);
        System.out.println("✅ AquaHaven: Seeded " + products.size() + " products across 7 categories.");
    }

    private Category cat(String name, String slug, String icon, int order) {
        Category c = new Category();
        c.setName(name); c.setSlug(slug); c.setIcon(icon);
        c.setSortOrder(order); c.setActive(true);
        return c;
    }

    private Product prod(String name, String desc, BigDecimal price,
                         Category cat, String waterType, int stock, String imageUrl) {
        Product p = new Product();
        p.setName(name); p.setDescription(desc); p.setPrice(price);
        p.setCategory(cat); p.setWaterType(waterType);
        p.setStock(stock); p.setImageUrl(imageUrl); p.setActive(true);
        return p;
    }
}