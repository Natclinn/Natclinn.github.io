package ontologyManagement;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.apache.jena.datatypes.xsd.XSDDatatype;
// utiliser le modele ontologique
import org.apache.jena.ontology.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;
import org.apache.jena.riot.RDFParser;
import org.apache.jena.vocabulary.DC;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.XSD;

public class CreateNatclinnTbox {

	public static <ValuesFromRestriction> void main( String[] args ) {

		String jsonString = CreationTBox();
		
		OntModel om = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
		
		RDFParser.create().fromString(jsonString).lang(Lang.JSONLD).parse(om);

		try {

			//////////////////////////////
			// Sorties fichiers         //
			////////////////////////////// 

			Path outputDir = Paths.get(System.getProperty("user.dir"), "NewGeneratedOntology");
			Files.createDirectories(outputDir);
			FileOutputStream outStream = new FileOutputStream(outputDir.resolve("ontology.owl").toFile());
			// exporte le resultat dans un fichier
			om.write(outStream, "RDF/XML");

			// N3 N-TRIPLE RDF/XML RDF/XML-ABBREV
			om.write(System.out, "N3");

			outStream.close();
		}
		catch (FileNotFoundException e) {System.out.println("File not found");}
		catch (IOException e) {System.out.println("IO problem");}
	}

	public static String CreationTBox() {

		String jsonString = null;

		OntModel om = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);

	    ///////////////////////////////
	    //Définition des namespaces  //
	    ///////////////////////////////
	    
	    String ncl = new String("https://natclinn.github.io/");
	    om.setNsPrefix("ncl", ncl);
		String aif = new String("http://www.arg.dundee.ac.uk/aif#");
	    om.setNsPrefix("aif", aif);
	    String dcat = new String("http://www.w3.org/ns/dcat#/");
	    om.setNsPrefix("dcat", dcat);
		String prov = new String("http://www.w3.org/ns/prov#");
	    om.setNsPrefix("prov", prov);
		String dc = new String("http://purl.org/dc/elements/1.1/");
	    om.setNsPrefix("dc", dc);
		String dct = new String("http://purl.org/dc/terms/");
	    om.setNsPrefix("dct", dct);
		String skos = new String("http://www.w3.org/2004/02/skos/core#");
	    om.setNsPrefix("skos", skos);
	    String foaf = new String("http://xmlns.com/foaf/0.1/");
	    om.setNsPrefix("foaf", foaf);
	    String rdfs = new String("http://www.w3.org/2000/01/rdf-schema#");
	    om.setNsPrefix("rdfs", rdfs);
	    String bibo = new String("http://purl.org/ontology/bibo/");
	    om.setNsPrefix("bibo", bibo);
	    String vann = new String("http://purl.org/vocab/vann/");
	    om.setNsPrefix("vann", vann);
	    String schema = new String("http://schema.org/");
	    om.setNsPrefix("schema", schema);
	    String org = new String("http://www.w3.org/ns/org#");
	    om.setNsPrefix("org", org);
	    String vocab = new String("https://w3id.org/afy/vocab#");
	    om.setNsPrefix("vocab", vocab);
		String pav = new String("http://purl.org/pav/");
	    om.setNsPrefix("pav", pav);



		/////////////////////////////////////
	    //Description de l'ontologie       //
	    /////////////////////////////////////
	    
	    Ontology ont = om.createOntology(ncl + "NatclinnTbox");
		ont.addProperty(RDFS.label, "Natcl'inn ontology", "en");
		ont.addProperty(RDFS.label, "Ontologie Natcl'inn", "fr");
		om.add(ont, DC.description,"Tbox for the Natcl'inn ontology", "en");
		om.add(ont, DC.description,"Tbox pour l'ontologie Natcl'inn", "fr");
		om.add(ont, DC.creator,"Raphael CONDE SALAZAR, Cedric Baudrit, Pierre BISQUERT, Christophe Fernandez, Rallou THOMOPOULOS, Magalie Weber");	
        
		ont.addProperty(OWL.versionIRI, om.createResource(ncl + "ncl/1.0.2"));
		ont.addProperty(OWL.versionInfo, "1.0.2");
		ont.addProperty(om.createProperty(pav + "previousVersion"), om.createResource(ncl + "ncl/1.0.1/ontology.owl"));




        //ont.addProperty(OWL.imports, om.createResource("http://www.w3.org/2000/01/rdf-schema"));

        // Titre
        ont.addProperty(om.createProperty(dc + "title"), "NCL", "en");

        // Abstracts
        ont.addProperty(om.createProperty(dct + "abstract"),
                "Le projet NATCL’INN vise à aider les entreprises agroalimentaires à mieux répondre aux attentes de naturalité des consommateurs, perçue comme gage de qualité, de santé et de goût. En s’appuyant sur la caractérisation des représentations de la naturalité et l’identification de marqueurs pertinents, il développe un outil d’arbitrage multicritère intégrant contraintes techniques, économiques et réglementaires. Porté par l’ADRIA, l’UBO-LEGO, l’INRAE et plusieurs partenaires industriels, ce projet collaboratif est labellisé par Valorial.", "fr");
        ont.addProperty(om.createProperty(dct + "abstract"),
                "The NATCL’INN project aims to help agri-food companies better meet consumers' expectations for naturalness, which is perceived as a guarantee of quality, health and taste. Based on the characterisation of representations of naturalness and the identification of relevant markers, it is developing a multi-criteria arbitration tool that integrates technical, economic and regulatory constraints. Led by ADRIA, UBO-LEGO, INRAE and several industrial partners, this collaborative project has been certified by Valorial.", "en");

        // Dates de création
        ont.addProperty(om.createProperty(dct + "created"), "Le 5 Octobre 2021", "fr");
        ont.addProperty(om.createProperty(dct + "created"), "October 5th, 2021", "en");

        // Licence et DOI
        ont.addProperty(om.createProperty(dct + "license"), om.createResource("https://www.data.gouv.fr/pages/legal/licences/etalab-2.0"));
        ont.addProperty(om.createProperty(bibo + "doi"), om.createResource("https://doi.org/10.57745/PKKGWE"));
        ont.addProperty(om.createProperty(bibo + "status"), om.createResource("http://purl.org/ontology/bibo/status/draft"));

        // Informations complémentaires
        ont.addProperty(om.createProperty(vann + "preferredNamespacePrefix"), "ncl", "en");
        ont.addProperty(om.createProperty(vann + "preferredNamespaceUri"), ncl);

        ont.addProperty(RDFS.comment, "Une ontologie conçue pour définir les différents produits de l'industrie agroalimentaire et les arguments qui leur sont associés en termes de naturalité perçue.", "fr");
        ont.addProperty(RDFS.comment, "An ontology designed to define the various products of the agri-food industry and the arguments associated with them in terms of their perceived naturalness.", "en");
        
        ont.addProperty(om.createProperty(foaf + "fundedBy"), om.createResource("https://www.pole-valorial.fr"));

        ont.addProperty(om.createProperty(schema + "citation"),
                "Cite this vocabulary as: Raphael Conde Salazar , Cedric Baudrit, Pierre Bisquert, Christophe Fernandez, Rallou Thomopoulos, Magalie Weber; HAL: https://hal.science/hal-05638309", "en");

		//ont.addProperty(om.createProperty(schema + "citation"),
        //        "Cite this vocabulary as: Raphaël Conde Salazar , Cédric Baudrit, Pierre Bisquert, Christophe Fernandez, Rallou Thomopoulos, Magalie Weber; HAL: https://hal.science/hal-05638309", "fr");
        
		// Introduction
        ont.addProperty(om.createProperty(vocab + "introduction"),
                "NATCL’INN vise à proposer une solution aux entreprises de l’agroalimentaire qui doivent réaliser des arbitrages entre différents attributs produits relatifs à la naturalité en vue de répondre aux nouvelles attentes et représentations des consommateurs en matière de naturalité alimentaire. Afin de proposer un prototype d’Outil d’Aide à la Décision, les équipes de R&D de l’ADRIA, le laboratoire LEGO de l’Université de Bretagne Occidentale (UBO), les unités de recherche INRAE et leurs partenaires industriels (Bridor, Paticeo, Charles Christ, La PAM, Ecomiam, Fleury Michon, Guyader Gastronomie) se sont associés pour mener un programme ambitieux de R&D collaborative. Le projet NATCL’INN a été labellisé par le pôle de compétitivité VALORIAL et a reçu le soutien de la Région Bretagne, de la Région Pays de la Loire et de Quimper Bretagne Occidentale. Le projet a démarré au 1er janvier 2024 et s'achèvera fin 2027.", "fr");
        ont.addProperty(om.createProperty(vocab + "introduction"),
                "NATCL’INN aims to offer a solution to agri-food companies that need to balance different product attributes related to naturalness in order to meet new consumer expectations and perceptions regarding food naturalness. In order to propose a prototype Decision Support Tool, the R&D teams at ADRIA, the LEGO laboratory at the University of Western Brittany (UBO), INRAE research units and their industrial partners (Bridor, Paticeo, Charles Christ, La PAM, Ecomiam, Fleury Michon, Guyader Gastronomie) have joined forces to carry out an ambitious collaborative R&D programme. The NATCL'INN project has been certified by the VALORIAL competitiveness cluster and has received support from the Brittany Region, the Pays de la Loire Region and Quimper Bretagne Occidentale. The project began on 1 January 2024 and will be completed at the end of 2027", "en");

        ont.addProperty(om.createProperty(vocab + "rdfxmlSerialization"),
                om.createTypedLiteral("https://w3id.org/NCL/ontology.xml",
                        XSDDatatype.XSDanyURI));

        // =====================
        // Définition des auteurs (noeuds anonymes)
        // =====================

        // Auteur 1
        Resource person1 = om.createResource();
        person1.addProperty(RDF.type, om.createResource(schema + "Person"));
        Resource org1 = om.createResource();
        org1.addProperty(RDF.type, om.createResource(foaf + "Organization"));
        org1.addProperty(om.createProperty(foaf + "name"), "University of Montpellier, INRAE, France");
        org1.addProperty(om.createProperty(schema + "url"), "https://www.umontpellier.fr/");
        person1.addProperty(om.createProperty(org + "memberOf"), org1);
        person1.addProperty(om.createProperty(schema + "familyName"), "Conde Salazar", "fr");
        person1.addProperty(om.createProperty(schema + "familyName"), "Conde Salazar", "en");
        //person1.addProperty(om.createProperty(schema + "name"), "Raphaël Conde Salazar", "fr");
        person1.addProperty(om.createProperty(schema + "name"), "Raphael Conde Salazar", "en");
        person1.addProperty(om.createProperty(schema + "url"), om.createResource("https://orcid.org/0000-0002-6926-5299"));
        ont.addProperty(om.createProperty(schema + "creator"), person1);

        // Auteur 2
        Resource person2 = om.createResource();
        person2.addProperty(RDF.type, om.createResource(schema + "Person"));
        Resource org2 = om.createResource();
        org2.addProperty(RDF.type, om.createResource(foaf + "Organization"));
        org2.addProperty(om.createProperty(foaf + "name"), "University of Montpellier, INRAE, France");
        org2.addProperty(om.createProperty(schema + "url"), "https://www.umontpellier.fr/");
        person2.addProperty(om.createProperty(org + "memberOf"), org2);
        person2.addProperty(om.createProperty(schema + "name"), "Pierre Bisquert", "fr");
        person2.addProperty(om.createProperty(schema + "name"), "Pierre Bisquert", "en");
        person2.addProperty(om.createProperty(schema + "url"), om.createResource("https://orcid.org/0000-0001-9418-5330" + //
						""));
        ont.addProperty(om.createProperty(schema + "creator"), person2);

        // Auteur 3
        Resource person3 = om.createResource();
        person3.addProperty(RDF.type, om.createResource(schema + "Person"));
        Resource org3 = om.createResource();
        org3.addProperty(RDF.type, om.createResource(foaf + "Organization"));
        org3.addProperty(om.createProperty(foaf + "name"), "University of Montpellier, INRAE, France");
        org3.addProperty(om.createProperty(schema + "url"), "https://www.umontpellier.fr/");
        person3.addProperty(om.createProperty(org + "memberOf"), org3);
        person3.addProperty(om.createProperty(schema + "name"), "Rallou Thomopoulos", "fr");
        person3.addProperty(om.createProperty(schema + "name"), "Rallou Thomopoulos", "en");
        person3.addProperty(om.createProperty(schema + "url"), om.createResource("https://orcid.org/0000-0002-3218-9472"));
        ont.addProperty(om.createProperty(schema + "creator"), person3);

		// Auteur 4
        Resource person4 = om.createResource();
        person4.addProperty(RDF.type, om.createResource(schema + "Person"));
        Resource org4 = om.createResource();
        org4.addProperty(RDF.type, om.createResource(foaf + "Organization"));
        org4.addProperty(om.createProperty(foaf + "name"), "INRAE, France");
        org4.addProperty(om.createProperty(schema + "url"), "https://www.inrae.fr/");
        person4.addProperty(om.createProperty(org + "memberOf"), org4);
        person4.addProperty(om.createProperty(schema + "name"), "Magalie Weber", "fr");
        person4.addProperty(om.createProperty(schema + "name"), "Magalie Weber", "en");
        person4.addProperty(om.createProperty(schema + "url"), om.createResource("https://orcid.org/0000-0001-6573-4070"));
        ont.addProperty(om.createProperty(schema + "creator"), person4);

		// Auteur 5
        Resource person5 = om.createResource();
        person5.addProperty(RDF.type, om.createResource(schema + "Person"));
        Resource org5 = om.createResource();
        org5.addProperty(RDF.type, om.createResource(foaf + "Organization"));
        org5.addProperty(om.createProperty(foaf + "name"), "INRAE, France");
        org5.addProperty(om.createProperty(schema + "url"), "https://www.inrae.fr/");
        person5.addProperty(om.createProperty(org + "memberOf"), org5);
        //person5.addProperty(om.createProperty(schema + "name"), "Cédric Baudrit", "fr");
        person5.addProperty(om.createProperty(schema + "name"), "Cedric Baudrit", "en");
        person5.addProperty(om.createProperty(schema + "url"), om.createResource("https://orcid.org/0000-0003-4320-3345"));
        ont.addProperty(om.createProperty(schema + "creator"), person5);

		// Auteur 6
        Resource person6 = om.createResource();
        person6.addProperty(RDF.type, om.createResource(schema + "Person"));
        Resource org6 = om.createResource();
        org6.addProperty(RDF.type, om.createResource(foaf + "Organization"));
        org6.addProperty(om.createProperty(foaf + "name"), "INRAE, France");	
        org6.addProperty(om.createProperty(schema + "url"), "https://www.inrae.fr/");
        person6.addProperty(om.createProperty(org + "memberOf"), org6);
        person6.addProperty(om.createProperty(schema + "name"), "Christophe Fernandez", "fr");
        person6.addProperty(om.createProperty(schema + "name"), "Christophe Fernandez", "en");
        person6.addProperty(om.createProperty(schema + "url"), om.createResource("https://orcid.org/0000-0001-6765-4680"));
        ont.addProperty(om.createProperty(schema + "creator"), person6);

        // Financement
        ont.addProperty(om.createProperty(schema + "funding"),
                om.createResource("https://www.pole-valorial.fr"));



	    //////////////////////////////////////////////////////////////////////
	    //														            //
	    //				TBOX = terminological box				            //				
	    //	ensemble de formules relatives aux informations terminologiques	//
	    //	(i.e. notions de bases et relations entre elles)                //
	    //       													        //
	    //////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////
//Axiomisation des propriétés et des relations	//
//terminologie DL: concepts et rôles	        // 
//////////////////////////////////////////////////

	    ////////////////////////////////////////////
	    // Définition des concepts atomiques      //
	    ////////////////////////////////////////////		

	    // Pour les éléments d'une structure décrivant un produit

		Property prefLabel = om.createProperty(skos + "prefLabel");

	    // NCL est un ensemble de produits et d'arguments
	    OntClass NCL = om.createClass(ncl + "NCL");
	    NCL.addComment("NCL is the set of product and arguments.", "en");
		NCL.addComment("NCL est l'ensemble des produits et des arguments.", "fr");
		addBilingualLabels(NCL, prefLabel, "NCL", "NCL");
	    
		OntClass Product = om.createClass(ncl + "Product");
		Product.addComment("A food industry product.", "en");
		Product.addComment("Un produit de l'industrie alimentaire.", "fr");
		addBilingualLabels(Product, prefLabel, "Product", "Produit");

		OntClass CompositeProduct = om.createClass(ncl + "CompositeProduct");
		CompositeProduct.addComment("Product composed of at least one other product.", "en");
		CompositeProduct.addComment("Produit composé d'au moins un autre produit.", "fr"); 
		addBilingualLabels(CompositeProduct, prefLabel, "Composite product", "Produit composite");

		OntClass SimpleProduct = om.createClass(ncl + "SimpleProduct");
		SimpleProduct.addComment("Food industry product made up entirely of ingredients.", "en");
		SimpleProduct.addComment("Produit de l'industrie alimentaire composé uniquement d'ingrédients.", "fr"); 
		addBilingualLabels(SimpleProduct, prefLabel, "Simple product", "Produit simple");


		OntClass Resource = om.createClass(ncl + "Resource");
		Resource.addComment("Abstract class Resource, from which Product and Ingredient inherit.", "en");
		Resource.addComment("Classe abstraite Resource, dont héritent Produit et Ingrédient.", "fr"); 
		addBilingualLabels(Resource, prefLabel, "Resource", "Ressource");

		// Pour les ingrédients 
		OntClass Ingredient = om.createClass(ncl + "Ingredient");
		Ingredient.addComment("An ingredient used in a product.", "en");
		Ingredient.addComment("Un ingrédient utilisé dans un produit.", "fr");
		addBilingualLabels(Ingredient, prefLabel, "Ingredient", "Ingrédient");

		OntClass CompositeIngredient = om.createClass(ncl + "CompositeIngredient");
		CompositeIngredient.addComment("Ingredient composed of at least one other Ingredient.", "en");
		CompositeIngredient.addComment("Un ingrédient composé d'au moins un autre ingrédient.", "fr"); 
		addBilingualLabels(CompositeIngredient, prefLabel, "Composite ingredient", "Ingrédient composite");

		OntClass SimpleIngredient = om.createClass(ncl + "SimpleIngredient");
		SimpleIngredient.addComment("A simple ingredient.", "en");
		SimpleIngredient.addComment("Un ingrédient simple.", "fr"); 
		addBilingualLabels(SimpleIngredient, prefLabel, "Simple ingredient", "Ingrédient simple");

		OntClass IngredientByOrigin = om.createClass(ncl + "IngredientByOrigin");
		IngredientByOrigin.addSuperClass(Ingredient);
		IngredientByOrigin.addComment("Ingredient classified by its origin.", "en");
		IngredientByOrigin.addComment("Ingrédient classé selon son origine.", "fr");
		addBilingualLabels(IngredientByOrigin, prefLabel, "Ingredient by origin", "Ingrédient par origine");

		OntClass IngredientByFunction = om.createClass(ncl + "IngredientByFunction");
		IngredientByFunction.addSuperClass(Ingredient);
		IngredientByFunction.addComment("Ingredient classified by its function in the food product.", "en");
		IngredientByFunction.addComment("Ingrédient classé selon sa fonction dans le produit alimentaire.", "fr");
		addBilingualLabels(IngredientByFunction, prefLabel, "Ingredient by function", "Ingrédient par fonction");

		OntClass IngredientByTransformationDegree = om.createClass(ncl + "IngredientByTransformationDegree");
		IngredientByTransformationDegree.addSuperClass(Ingredient);
		IngredientByTransformationDegree.addComment("Ingredient classified by its degree of processing.", "en");
		IngredientByTransformationDegree.addComment("Ingrédient classé selon son degré de transformation.", "fr");
		addBilingualLabels(IngredientByTransformationDegree, prefLabel, "Ingredient by transformation degree", "Ingrédient par degré de transformation");

		// Origine
		OntClass PlantOriginIngredient = om.createClass(ncl + "PlantOriginIngredient");
		PlantOriginIngredient.addSuperClass(IngredientByOrigin);
		PlantOriginIngredient.addComment("Ingredient of plant origin.", "en");
		PlantOriginIngredient.addComment("Ingrédient d'origine végétale.", "fr");
		addBilingualLabels(PlantOriginIngredient, prefLabel, "Plant origin ingredient", "Ingrédient d'origine végétale");

		OntClass AnimalOriginIngredient = om.createClass(ncl + "AnimalOriginIngredient");
		AnimalOriginIngredient.addSuperClass(IngredientByOrigin);
		AnimalOriginIngredient.addComment("Ingredient of animal origin.", "en");
		AnimalOriginIngredient.addComment("Ingrédient d'origine animale.", "fr");
		addBilingualLabels(AnimalOriginIngredient, prefLabel, "Animal origin ingredient", "Ingrédient d'origine animale");

		OntClass MineralOriginIngredient = om.createClass(ncl + "MineralOriginIngredient");
		MineralOriginIngredient.addSuperClass(IngredientByOrigin);
		MineralOriginIngredient.addComment("Ingredient of mineral origin.", "en");
		MineralOriginIngredient.addComment("Ingrédient d'origine minérale.", "fr");
		addBilingualLabels(MineralOriginIngredient, prefLabel, "Mineral origin ingredient", "Ingrédient d'origine minérale");

		OntClass FungalOrMicrobialIngredient = om.createClass(ncl + "FungalOrMicrobialIngredient");
		FungalOrMicrobialIngredient.addSuperClass(IngredientByOrigin);
		FungalOrMicrobialIngredient.addComment("Ingredient of fungal or microbial origin.", "en");
		FungalOrMicrobialIngredient.addComment("Ingrédient d'origine fongique ou microbienne.", "fr");
		addBilingualLabels(FungalOrMicrobialIngredient, prefLabel, "Fungal or microbial ingredient", "Ingrédient d'origine fongique ou microbienne");

		OntClass SyntheticOrBiotechIngredient = om.createClass(ncl + "SyntheticOrBiotechIngredient");
		SyntheticOrBiotechIngredient.addSuperClass(IngredientByOrigin);
		SyntheticOrBiotechIngredient.addComment("Ingredient of synthetic or biotechnological origin.", "en");
		SyntheticOrBiotechIngredient.addComment("Ingrédient d'origine synthétique ou biotechnologique.", "fr");
		addBilingualLabels(SyntheticOrBiotechIngredient, prefLabel, "Synthetic or biotech ingredient", "Ingrédient d'origine synthétique ou biotechnologique");

		// Fonction
		OntClass MainComponentIngredient = om.createClass(ncl + "MainComponentIngredient");
		MainComponentIngredient.addSuperClass(IngredientByFunction);
		MainComponentIngredient.addComment("Main ingredient providing structure or mass.", "en");
		MainComponentIngredient.addComment("Ingrédient principal apportant structure ou masse.", "fr");
		addBilingualLabels(MainComponentIngredient, prefLabel, "Main component ingredient", "Ingrédient composant principal");

		OntClass AdditiveIngredient = om.createClass(ncl + "AdditiveIngredient");
		AdditiveIngredient.addSuperClass(IngredientByFunction);
		AdditiveIngredient.addComment("Ingredient used as an additive (e.g., colorant, preservative).", "en");
		AdditiveIngredient.addComment("Ingrédient utilisé comme additif (colorant, conservateur…).", "fr");
		addBilingualLabels(AdditiveIngredient, prefLabel, "Additive ingredient", "Ingrédient additif");

		OntClass FlavorIngredient = om.createClass(ncl + "FlavorIngredient");
		FlavorIngredient.addSuperClass(IngredientByFunction);
		FlavorIngredient.addComment("Ingredient used for flavoring purposes.", "en");
		FlavorIngredient.addComment("Ingrédient utilisé pour aromatiser.", "fr");
		addBilingualLabels(FlavorIngredient, prefLabel, "Flavor ingredient", "Ingrédient aromatique");

		OntClass EnzymeIngredient = om.createClass(ncl + "EnzymeIngredient");
		EnzymeIngredient.addSuperClass(IngredientByFunction);
		EnzymeIngredient.addComment("Ingredient used as an enzyme in food processing.", "en");
		EnzymeIngredient.addComment("Ingrédient utilisé comme enzyme dans le procédé alimentaire.", "fr");
		addBilingualLabels(EnzymeIngredient, prefLabel, "Enzyme ingredient", "Ingrédient enzymatique");

		OntClass FunctionalNutrientIngredient = om.createClass(ncl + "FunctionalNutrientIngredient");
		FunctionalNutrientIngredient.addSuperClass(IngredientByFunction);
		FunctionalNutrientIngredient.addComment("Ingredient providing nutritional or health benefits.", "en");
		FunctionalNutrientIngredient.addComment("Ingrédient apportant des bénéfices nutritionnels ou santé.", "fr");
		addBilingualLabels(FunctionalNutrientIngredient, prefLabel, "Functional nutrient ingredient", "Ingrédient nutriment fonctionnel");

		OntClass TechnologicalIngredient = om.createClass(ncl + "TechnologicalIngredient");
		TechnologicalIngredient.addSuperClass(IngredientByFunction);
		TechnologicalIngredient.addComment("Ingredient used for technological purposes (e.g., carrier, thickener).", "en");
		TechnologicalIngredient.addComment("Ingrédient utilisé pour des raisons technologiques (support, épaississant…).", "fr");
		addBilingualLabels(TechnologicalIngredient, prefLabel, "Technological ingredient", "Ingrédient technologique");

		// Degré de transformation
		OntClass RawIngredient = om.createClass(ncl + "RawIngredient");
		RawIngredient.addSuperClass(IngredientByTransformationDegree);
		RawIngredient.addComment("Unprocessed or minimally processed ingredient.", "en");
		RawIngredient.addComment("Ingrédient brut ou peu transformé.", "fr");
		addBilingualLabels(RawIngredient, prefLabel, "Raw ingredient", "Ingrédient brut");

		OntClass ProcessedIngredient = om.createClass(ncl + "ProcessedIngredient");
		ProcessedIngredient.addSuperClass(IngredientByTransformationDegree);
		ProcessedIngredient.addComment("Ingredient that has been processed (e.g., flour, oil).", "en");
		ProcessedIngredient.addComment("Ingrédient ayant subi une transformation (farine, huile…).", "fr");
		addBilingualLabels(ProcessedIngredient, prefLabel, "Processed ingredient", "Ingrédient transformé");

		OntClass UltraProcessedIngredient = om.createClass(ncl + "UltraProcessedIngredient");
		UltraProcessedIngredient.addSuperClass(IngredientByTransformationDegree);
		UltraProcessedIngredient.addComment("Ingredient that is highly processed or modified.", "en");
		UltraProcessedIngredient.addComment("Ingrédient hautement transformé ou modifié.", "fr");
		addBilingualLabels(UltraProcessedIngredient, prefLabel, "Ultra-processed ingredient", "Ingrédient ultra-transformé");

		OntClass QuantifiedElement = om.createClass(ncl + "QuantifiedElement");
		QuantifiedElement.addComment("An element of a product composition that includes quantity, unit, percentage, and rank.", "en");
		QuantifiedElement.addComment("Un élément d'une composition de produit incluant quantité, unité, pourcentage et rang.", "fr");
		addBilingualLabels(QuantifiedElement, prefLabel, "Quantified element", "Élément quantifié");

		OntClass Packaging = om.createClass(ncl + "Packaging");
		Packaging.addComment("The packaging of a product.", "en");
		Packaging.addComment("L'emballage d'un produit.", "fr");
		addBilingualLabels(Packaging, prefLabel, "Packaging", "Emballage");

		OntClass Shape = om.createClass(ncl + "Shape");
		Shape.addComment("The forme used for the product packaging.", "en");
		Shape.addComment("La forme de l'emballage du produit.", "fr");
		addBilingualLabels(Shape, prefLabel, "Shape", "Forme");

		OntClass Material = om.createClass(ncl + "Material");
		Material.addComment("The material used for the product packaging.", "en");
		Material.addComment("La matière de l'emballage du produit.", "fr");
		addBilingualLabels(Material, prefLabel, "Material", "Matière");

		OntClass Allegation = om.createClass(ncl + "Allegation");
		Allegation.addComment("Statement about a product claim or assertion.", "en");
		Allegation.addComment("Déclaration relative à une allégation ou affirmation sur un produit.", "fr");
		addBilingualLabels(Allegation, prefLabel, "Allegation", "Allégation");

		OntClass ControlledOriginLabel = om.createClass(ncl + "ControlledOriginLabel");
		ControlledOriginLabel.addComment("The controlled origin label of a product.", "en");
		ControlledOriginLabel.addComment("Le label d'origine contrôlée d'un produit.", "fr");
		addBilingualLabels(ControlledOriginLabel, prefLabel, "Controlled origin label", "Label d'origine contrôlée");

		OntClass CleanLabel = om.createClass(ncl + "CleanLabel");
		CleanLabel.addComment("The clean label of a product.", "en");
		CleanLabel.addComment("Le 'clean label' d'un produit.", "fr");
		addBilingualLabels(CleanLabel, prefLabel, "Clean label", "Clean label");

		OntClass ManufacturingProcess = om.createClass(ncl + "ManufacturingProcess");
		ManufacturingProcess.addComment("The way the product is manufactured.", "en");
		ManufacturingProcess.addComment("La manière dont le produit est fabriqué.", "fr");
		addBilingualLabels(ManufacturingProcess, prefLabel, "Manufacturing process", "Procédé de fabrication");

		OntClass FNI = om.createClass(ncl + "FNI");
		FNI.addComment("food naturalness index.", "en");
		FNI.addComment("Indice de naturalité d'un produit alimentaire.", "fr");
		addBilingualLabels(FNI, prefLabel, "Food naturalness index", "Indice de naturalité alimentaire");
		 
		OntClass NutriScore = om.createClass(ncl + "NutriScore");
		NutriScore.addComment("Nutri-Score.", "en");
		NutriScore.addComment("Nutri-Score.", "fr");
		addBilingualLabels(NutriScore, prefLabel, "Nutri-Score", "Nutri-Score");

		OntClass NutriScoreAlpha = om.createClass(ncl + "NutriScoreAlpha");
		NutriScoreAlpha.addComment("Nutri-Score alpha	.", "en");
		NutriScoreAlpha.addComment("Nutri-Score alpha.", "fr");
		addBilingualLabels(NutriScoreAlpha, prefLabel, "Nutri-Score alpha", "Nutri-Score alpha");

		OntClass NutriScoreDetail = om.createClass(ncl + "NutriScoreDetail");
		NutriScoreDetail.addComment("Nutri-Score detail	.", "en");
		NutriScoreDetail.addComment("Nutri-Score detail.", "fr");
		addBilingualLabels(NutriScoreDetail, prefLabel, "Nutri-Score detail", "Détail Nutri-Score");

		OntClass NOVAgroupDetails = om.createClass(ncl + "NOVAgroupDetails");
		NOVAgroupDetails.addComment("Details of NOVA group classification from Open Food Facts.", "en");
		NOVAgroupDetails.addComment("Détails de la classification NOVA depuis Open Food Facts.", "fr");
		addBilingualLabels(NOVAgroupDetails, prefLabel, "NOVA group details", "Détails du groupe NOVA");

		OntClass CalculatedNOVAgroupDetails = om.createClass(ncl + "CalculatedNOVAgroupDetails");
		CalculatedNOVAgroupDetails.addComment("Details of locally calculated NOVA group, listing ingredients by category.", "en");
		CalculatedNOVAgroupDetails.addComment("Détails du groupe NOVA calculé localement, listant les ingrédients par catégorie.", "fr");
		addBilingualLabels(CalculatedNOVAgroupDetails, prefLabel, "Calculated NOVA group details", "Détails du groupe NOVA calculé");

		OntClass NaturalnessScore = om.createClass(ncl + "NaturalnessScore");
		NaturalnessScore.addComment("Score resource representing the overall naturalness level of a product.", "en");
		NaturalnessScore.addComment("Ressource de score représentant le niveau global de naturalité d'un produit.", "fr");
		addBilingualLabels(NaturalnessScore, prefLabel, "Naturalness score", "Score de naturalité");

		OntClass Origin = om.createClass(ncl + "Origin");
		Origin.addComment("Ingredient origin (EU origin, Vietnam origin, etc.).", "en");
		Origin.addComment("Origine de l'ingrédient (origine UE, origine Vietnam, etc.).", "fr");
		addBilingualLabels(Origin, prefLabel, "Origin", "Origine");
		// Pour les arguments 

		OntClass ProductArgument = om.createClass(ncl + "ProductArgument");
		ProductArgument.addComment("An argument related to the naturalness of a product.", "en");
		ProductArgument.addComment("Un argument lié à la naturalité d'un produit.", "fr");
		addBilingualLabels(ProductArgument, prefLabel, "Product argument", "Argument produit");

		OntClass LinkToArgument = om.createClass(ncl + "LinkToArgument");
		LinkToArgument.addComment("A link between a product and an argument, containing metadata about the relationship.", "en");
		LinkToArgument.addComment("Un lien entre un produit et un argument, contenant des métadonnées sur la relation.", "fr");
		addBilingualLabels(LinkToArgument, prefLabel, "Link to argument", "Lien vers argument");

		OntClass Tag = om.createClass(ncl + "Tag");
		Tag.addComment("A tag used to qualify product argument links.", "en");
		Tag.addComment("Un tag utilisé pour qualifier les liens d'argumentation produit.", "fr");
		addBilingualLabels(Tag, prefLabel, "Tag", "Tag");

		OntClass TagArgumentBinding = om.createClass(ncl + "TagArgumentBinding");
		TagArgumentBinding.addComment("Binding object carrying argumentation attributes associated with a tag.", "en");
		TagArgumentBinding.addComment("Objet de liaison portant les attributs d'argumentation associés à un tag.", "fr");
		addBilingualLabels(TagArgumentBinding, prefLabel, "Tag argument binding", "Liaison tag-argument");

		OntClass Stakeholder = om.createClass(ncl + "Stakeholder");
		Stakeholder.addComment("The stakeholder making the argument (consumer, manufacturer, etc.).", "en");
		Stakeholder.addComment("Le partie prenante faisant l'argument (consommateur, fabricant, etc.).", "fr");
		addBilingualLabels(Stakeholder, prefLabel, "Stakeholder", "Partie prenante");
		
		OntClass Source = om.createClass(ncl + "Source");
		Source.addComment("The source of an argument (consumer, scientific paper, etc.).", "en");
		Source.addComment("La source d'un argument (consommateur, article scientifique, etc.).", "fr");
		addBilingualLabels(Source, prefLabel, "Source", "Source");

		OntClass TypeSource = om.createClass(ncl + "TypeSource");
		TypeSource.addComment("The type of source of an argument (expert, peer-reviewed article, etc.).", "en");
		TypeSource.addComment("Le type de source d'un argument (expert, article peer-reviewed, etc.).", "fr");
		addBilingualLabels(TypeSource, prefLabel, "Source type", "Type de source");

		OntClass Context = om.createClass(ncl + "Context");
		Context.addComment("The context in which the argument is relevant.", "en");
		Context.addComment("Le contexte dans lequel l'argument est pertinent.", "fr");
		addBilingualLabels(Context, prefLabel, "Context", "Contexte");

		OntClass ContextProduct = om.createClass(ncl + "ContextProduct");
		ContextProduct.addSuperClass(Context);
		ContextProduct.addComment("The product context in which an argument is relevant.", "en");
		ContextProduct.addComment("Le contexte produit dans lequel un argument est pertinent.", "fr");
		addBilingualLabels(ContextProduct, prefLabel, "Product context", "Contexte produit");

		OntClass ContextIngredient = om.createClass(ncl + "ContextIngredient");
		ContextIngredient.addSuperClass(Context);
		ContextIngredient.addComment("The ingredient context in which an argument is relevant.", "en");
		ContextIngredient.addComment("Le contexte ingrédient dans lequel un argument est pertinent.", "fr");
		addBilingualLabels(ContextIngredient, prefLabel, "Ingredient context", "Contexte ingrédient");

		OntClass Attribute = om.createClass(ncl + "Attribute");
		Attribute.addComment("The attribute of naturalness in the context of the argument.", "en");
		Attribute.addComment("L'attribut de naturalité dans le contexte de l'argument.", "fr");
		addBilingualLabels(Attribute, prefLabel, "Attribute", "Attribut");

		OntClass Category = om.createClass(ncl + "Category");
		Category.addComment("Main category of the argument (e.g., 'Mode de culture et d'élevage').", "en");
		Category.addComment("Catégorie principale de l'argument (ex: 'Mode de culture et d'élevage').", "fr");
		addBilingualLabels(Category, prefLabel, "Category", "Catégorie");

		OntClass Subcategory = om.createClass(ncl + "Subcategory");
		Subcategory.addComment("Subcategory of the argument.", "en");
		Subcategory.addComment("Sous-catégorie de l'argument.", "fr");
		addBilingualLabels(Subcategory, prefLabel, "Subcategory", "Sous-catégorie");

		OntClass Verbatim = om.createClass(ncl + "Verbatim");
		Verbatim.addComment("Extract of a sentence supporting the argument.", "en");
		Verbatim.addComment("Extrait d'une phrase soutenant l'argument.", "fr");
		addBilingualLabels(Verbatim, prefLabel, "Verbatim", "Verbatim");

		// Pour les arguments avec AIF

		// Abstract class Node
        OntClass Node = om.createClass(aif + "Node");
        Node.addComment("Abstract class for argumentation nodes.", "en");
		Node.addComment("Un nœud dans un graphe d'argumentation.", "fr");
		addBilingualLabels(Node, prefLabel, "Argumentation node", "Noeud d'argumentation");

		// Add explicit bilingual labels for external classes referenced in this ontology.
		addBilingualLabels(om.createResource(schema + "Person"), prefLabel, "Person", "Personne");
		om.add(om.createResource(schema + "Person"), RDFS.comment, "A person involved in authorship or provenance metadata.", "en");
		om.add(om.createResource(schema + "Person"), RDFS.comment, "Une personne impliquée dans les métadonnées d'auteur ou de provenance.", "fr");
		addBilingualLabels(om.createResource(foaf + "Organization"), prefLabel, "Organization", "Organisation");
		om.add(om.createResource(foaf + "Organization"), RDFS.comment, "An organization involved in authorship, affiliation, or funding metadata.", "en");
		om.add(om.createResource(foaf + "Organization"), RDFS.comment, "Une organisation impliquée dans les métadonnées d'auteur, d'affiliation ou de financement.", "fr");

		
	    ////////////////////////////////////////////
	    // Définition des disjonctions de classes //
	    ////////////////////////////////////////////

	    // add disjoint individuals axiom assertion:
	   
	   List<OntClass> classes = Arrays.asList(
			ProductArgument,
			Tag,
			TagArgumentBinding,
			CleanLabel,
			Context,
			Attribute,
			Category,
    		Subcategory,
			ControlledOriginLabel,
			FNI,
			Ingredient,
			QuantifiedElement,
			ManufacturingProcess,
			NutriScore,
			NutriScoreDetail,
			NutriScoreAlpha,
			NOVAgroupDetails,
			CalculatedNOVAgroupDetails,
			NaturalnessScore,
			Allegation,
			Packaging,
			Shape,
			Material,
			Origin,
			Product,
			Source,
			TypeSource,
			Stakeholder,
			Verbatim,
			LinkToArgument
			
		);		// Création de toutes les disjonctions possibles entre chaque paire de classes
		for (int i = 0; i < classes.size(); i++) {
			for (int j = i + 1; j < classes.size(); j++) {
				classes.get(i).addDisjointWith(classes.get(j));
			}
		}

	    //////////////////////////////////////////////////////////
	    // Définition des object property                       //
	    //////////////////////////////////////////////////////////
		
		// anonymous class for unionOf
        RDFList unionListComposedOfDomain = om.createList(new RDFNode[] {CompositeIngredient, CompositeProduct});
        Resource unionClassComposedOfDomain = om.createResource()
            .addProperty(OWL.unionOf, unionListComposedOfDomain);
		RDFList unionListComposedOfRange = om.createList(new RDFNode[] {Ingredient, Product});
        Resource unionClassComposedOfRange = om.createResource()
            .addProperty(OWL.unionOf, unionListComposedOfRange);	
	    ObjectProperty isComposedOf = om.createObjectProperty(ncl + "isComposedOf");
		isComposedOf.addDomain(unionClassComposedOfDomain);
		isComposedOf.addRange(unionClassComposedOfRange);
		isComposedOf.addComment("Relates a composite resource to the resources it is composed of.", "en");
		isComposedOf.addComment("Relie une ressource composite aux ressources qui la composent.", "fr");
		addBilingualLabels(isComposedOf, prefLabel, "Is composed of", "Est composé de");

		ObjectProperty identifier = om.createObjectProperty(ncl + "identifier");
		identifier.addDomain(Resource);
		identifier.addRange(om.createResource(XSD.xstring.getURI()));
		identifier.addComment("Identifier associated with a resource.", "en");
		identifier.addComment("Identifiant associé à une ressource.", "fr");
		addBilingualLabels(identifier, prefLabel, "Identifier", "Identifiant");
		
		ObjectProperty hasIngredient = om.createObjectProperty(ncl + "hasIngredient");
		hasIngredient.addDomain(Product);
		hasIngredient.addRange(Ingredient);
		hasIngredient.addComment("Links a product to one of its ingredients.", "en");
		hasIngredient.addComment("Relie un produit à l'un de ses ingrédients.", "fr");
		addBilingualLabels(hasIngredient, prefLabel, "Has ingredient", "A pour ingrédient");

		ObjectProperty hasPackaging = om.createObjectProperty(ncl + "hasPackaging");
		hasPackaging.addDomain(Product);
		hasPackaging.addRange(Packaging);
		hasPackaging.addComment("Links a product to its packaging.", "en");
		hasPackaging.addComment("Relie un produit à son emballage.", "fr");
		addBilingualLabels(hasPackaging, prefLabel, "Has packaging", "A pour emballage");

		ObjectProperty hasMaterial = om.createObjectProperty(ncl + "hasMaterial");
		hasMaterial.addDomain(Packaging);
		hasMaterial.addRange(Material);
		hasMaterial.addComment("Links a packaging to the material it uses.", "en");
		hasMaterial.addComment("Relie un emballage au matériau qu'il utilise.", "fr");
		addBilingualLabels(hasMaterial, prefLabel, "Has material", "A pour matière");

		ObjectProperty hasShape = om.createObjectProperty(ncl + "hasShape");
		hasShape.addDomain(Packaging);
		hasShape.addRange(Shape);
		hasShape.addComment("Links a packaging to its shape.", "en");
		hasShape.addComment("Relie un emballage à sa forme.", "fr");
		addBilingualLabels(hasShape, prefLabel, "Has shape", "A pour forme");

		ObjectProperty hasAllegation = om.createObjectProperty(ncl + "hasAllegation");
		hasAllegation.addDomain(Product);
		hasAllegation.addRange(Allegation);
		hasAllegation.addComment("Links a product to its allegations or claims.", "en");
		hasAllegation.addComment("Relie un produit à ses allégations.", "fr");
		addBilingualLabels(hasAllegation, prefLabel, "Has allegation", "A pour allégation");

		ObjectProperty hasCleanLabel = om.createObjectProperty(ncl + "hasCleanLabel");
		hasCleanLabel.addDomain(Product);
		hasCleanLabel.addRange(CleanLabel);
		hasCleanLabel.addComment("Links a product to clean-label information.", "en");
		hasCleanLabel.addComment("Relie un produit à une information de clean label.", "fr");
		addBilingualLabels(hasCleanLabel, prefLabel, "Has clean label", "A pour clean label");

		ObjectProperty hasManufacturingProcess = om.createObjectProperty(ncl + "hasManufacturingProcess");
		hasManufacturingProcess.addDomain(Product);
		hasManufacturingProcess.addRange(ManufacturingProcess);
		hasManufacturingProcess.addComment("Links a product to its manufacturing process.", "en");
		hasManufacturingProcess.addComment("Relie un produit à son procédé de fabrication.", "fr");
		addBilingualLabels(hasManufacturingProcess, prefLabel, "Has manufacturing process", "A pour procédé de fabrication");

		ObjectProperty hasNutriScore = om.createObjectProperty(ncl + "hasNutriScore");
		hasNutriScore.addDomain(Product);
		hasNutriScore.addRange(NutriScore);
		hasNutriScore.addComment("Links a product to its Nutri-Score resource.", "en");
		hasNutriScore.addComment("Relie un produit à sa ressource Nutri-Score.", "fr");
		addBilingualLabels(hasNutriScore, prefLabel, "Has Nutri-Score", "A pour Nutri-Score");

		ObjectProperty hasNOVAgroupDetails = om.createObjectProperty(ncl + "hasNOVAgroupDetails");
		hasNOVAgroupDetails.addDomain(Product);
		hasNOVAgroupDetails.addRange(NOVAgroupDetails);
		hasNOVAgroupDetails.addComment("Links a product to its NOVA group classification details.", "en");
		hasNOVAgroupDetails.addComment("Relie un produit aux détails de sa classification NOVA.", "fr");
		addBilingualLabels(hasNOVAgroupDetails, prefLabel, "Has NOVA group details", "A pour détails du groupe NOVA");

		ObjectProperty hasCalculatedNOVAgroupDetails = om.createObjectProperty(ncl + "hasCalculatedNOVAgroupDetails");
		hasCalculatedNOVAgroupDetails.addDomain(Product);
		hasCalculatedNOVAgroupDetails.addRange(CalculatedNOVAgroupDetails);
		hasCalculatedNOVAgroupDetails.addComment("Links a product to its calculated NOVA group details.", "en");
		hasCalculatedNOVAgroupDetails.addComment("Relie un produit aux détails de son groupe NOVA calculé.", "fr");
		addBilingualLabels(hasCalculatedNOVAgroupDetails, prefLabel, "Has calculated NOVA group details", "A pour détails du groupe NOVA calculé");

		ObjectProperty hasNaturalnessScore = om.createObjectProperty(ncl + "hasNaturalnessScore");
		hasNaturalnessScore.addDomain(Product);
		hasNaturalnessScore.addRange(NaturalnessScore);
		hasNaturalnessScore.addComment("Links a product to its naturalness score resource.", "en");
		hasNaturalnessScore.addComment("Relie un produit à sa ressource de score de naturalité.", "fr");
		addBilingualLabels(hasNaturalnessScore, prefLabel, "Has naturalness score", "A pour score de naturalité");

		ObjectProperty hasFNI = om.createObjectProperty(ncl + "hasFNI");
		hasFNI.addDomain(Product);
		hasFNI.addRange(FNI);
		hasFNI.addComment("Links a product to its Food Naturalness Index resource.", "en");
		hasFNI.addComment("Relie un produit à sa ressource d'indice de naturalité alimentaire.", "fr");
		addBilingualLabels(hasFNI, prefLabel, "Has FNI", "A pour FNI");


		ObjectProperty hasQuantifiedElement = om.createObjectProperty(ncl + "hasQuantifiedElement");
		hasQuantifiedElement.addDomain(Resource);
		hasQuantifiedElement.addRange(QuantifiedElement);
		hasQuantifiedElement.addComment("Links a resource to one of its quantified elements.", "en");
		hasQuantifiedElement.addComment("Relie une ressource à l'un de ses éléments quantifiés.", "fr");
		addBilingualLabels(hasQuantifiedElement, prefLabel, "Has quantified element", "A pour élément quantifié");

		ObjectProperty hasOrigin = om.createObjectProperty(ncl + "hasOrigin");
		hasOrigin.addDomain(Ingredient);
		hasOrigin.addRange(Origin);
		hasOrigin.addComment("Links an ingredient to its origin.", "en");
		hasOrigin.addComment("Relie un ingrédient à son origine.", "fr");
		addBilingualLabels(hasOrigin, prefLabel, "Has origin", "A pour origine");


		ObjectProperty hasControlledOrigin = om.createObjectProperty(ncl + "hasControlledOriginLabel");
		hasControlledOrigin.addDomain(Product);
		hasControlledOrigin.addRange(ControlledOriginLabel);
		hasControlledOrigin.addComment("Links a product to a controlled-origin label.", "en");
		hasControlledOrigin.addComment("Relie un produit à un label d'origine contrôlée.", "fr");
		addBilingualLabels(hasControlledOrigin, prefLabel, "Has controlled origin label", "A pour label d'origine contrôlée");


		// anonymous class for unionOf
        RDFList unionListQuantifiedElement = om.createList(new RDFNode[] {Ingredient, Product});
        Resource unionClassQuantifiedElement = om.createResource()
            .addProperty(OWL.unionOf, unionListQuantifiedElement);
		ObjectProperty refersTo = om.createObjectProperty(ncl + "refersTo");
		refersTo.addDomain(QuantifiedElement);
		refersTo.addRange(unionClassQuantifiedElement);
		refersTo.addComment("Relates a product or ingredient to its quantification in a food industry product.", "en");
		refersTo.addComment("Relie un produit ou un ingrédient à sa quantification dans un produit de l'industrie agro-alimentaire.", "fr");
		addBilingualLabels(refersTo, prefLabel, "Refers to", "Réfère à");

	ObjectProperty hasProductArgument = om.createObjectProperty(ncl + "hasProductArgument");
	hasProductArgument.addDomain(Product);
	hasProductArgument.addRange(ProductArgument);
	hasProductArgument.addComment("Link a food industry product to an argument.", "en");
	hasProductArgument.addComment("Relie un produit de l'industrie agro-alimentaire à un argument.", "fr");
	addBilingualLabels(hasProductArgument, prefLabel, "Has product argument", "A pour argument produit");

	// New architecture with LinkToArgument intermediate entity
	ObjectProperty hasLinkToArgument = om.createObjectProperty(ncl + "hasLinkToArgument");
	hasLinkToArgument.addDomain(Product);
	hasLinkToArgument.addRange(LinkToArgument);
	hasLinkToArgument.addComment("Links a food industry product to a LinkToArgument that connects to a ProductArgument.", "en");
	hasLinkToArgument.addComment("Relie un produit de l'industrie agro-alimentaire à un LinkToArgument qui se connecte à un ProductArgument.", "fr");
	addBilingualLabels(hasLinkToArgument, prefLabel, "Has link to argument", "A pour lien vers argument");

	// LinkToArgument properties
	ObjectProperty hasTagInitiator = om.createObjectProperty(ncl + "hasTagInitiator");
	hasTagInitiator.addDomain(LinkToArgument);
	hasTagInitiator.addRange(Tag);
	hasTagInitiator.addComment("Tag identifying who initiated the link to the argument.", "en");
	hasTagInitiator.addComment("Tag identifiant qui a initié le lien vers l'argument.", "fr");	
	addBilingualLabels(hasTagInitiator, prefLabel, "Has tag initiator", "A pour tag initiateur");

		// LinkToArgument properties
	ObjectProperty hasTagArgumentBindingInitiator = om.createObjectProperty(ncl + "hasTagArgumentBindingInitiator");
	hasTagArgumentBindingInitiator.addDomain(LinkToArgument);
	hasTagArgumentBindingInitiator.addRange(TagArgumentBinding);
	hasTagArgumentBindingInitiator.addComment("The tag argument binding identifying who initiated the link to the argument.", "en");
	hasTagArgumentBindingInitiator.addComment("Le tag de liaison d'argument identifiant qui a initié le lien vers l'argument.", "fr");	
	addBilingualLabels(hasTagArgumentBindingInitiator, prefLabel, "Has tag argument binding initiator", "A pour liaison tag-argument initiatrice");

	ObjectProperty hasTag = om.createObjectProperty(ncl + "hasTag");
	hasTag.addDomain(Product);
	hasTag.addRange(Tag);
	hasTag.addComment("Links a product to a tag.", "en");
	hasTag.addComment("Relie un produit à un tag.", "fr");
	addBilingualLabels(hasTag, prefLabel, "Has tag", "A pour tag");

	ObjectProperty hasTagCheck = om.createObjectProperty(ncl + "hasTagCheck");
	hasTagCheck.addDomain(Product);
	hasTagCheck.addRange(Tag);
	hasTagCheck.addComment("Links a product to a validation tag.", "en");
	hasTagCheck.addComment("Relie un produit à un tag de vérification.", "fr");
	addBilingualLabels(hasTagCheck, prefLabel, "Has tag check", "A pour tag de vérification");

	ObjectProperty aboutTag = om.createObjectProperty(ncl + "aboutTag");
	aboutTag.addDomain(TagArgumentBinding);
	aboutTag.addRange(Tag);
	aboutTag.addComment("Links a TagArgumentBinding to the related Tag.", "en");
	aboutTag.addComment("Relie un TagArgumentBinding au Tag concerné.", "fr");
	addBilingualLabels(aboutTag, prefLabel, "About tag", "Concerne le tag");

	ObjectProperty hasReferenceProductArgument = om.createObjectProperty(ncl + "hasReferenceProductArgument");
	hasReferenceProductArgument.addDomain(LinkToArgument);
	hasReferenceProductArgument.addRange(ProductArgument);
	hasReferenceProductArgument.addComment("Links a LinkToArgument to the referenced ProductArgument.", "en");
	hasReferenceProductArgument.addComment("Relie un LinkToArgument au ProductArgument référencé.", "fr");
	addBilingualLabels(hasReferenceProductArgument, prefLabel, "Has reference product argument", "A pour argument produit référencé");

	RDFList unionListTarget = om.createList(new RDFNode[] { Ingredient, Product, Packaging, Allegation, CleanLabel, ManufacturingProcess, NutriScore, Origin, ControlledOriginLabel});
		Resource unionClassTarget = om.createResource()
		 .addProperty(OWL.unionOf, unionListTarget);
		ObjectProperty target = om.createObjectProperty(ncl + "target");
		target.addDomain(ProductArgument);
		target.addRange(unionClassTarget);
		target.addComment("Link an argument to a component (product, ingredient, packaging, nutri-score, etc.) of a food industry product.", "en");
		target.addComment("Relie un argument à un composant (produit, ingrédient, packaging, nutri-score,etc.) d'un produit de l'industrie agro-alimentaire.", "fr");
		addBilingualLabels(target, prefLabel, "Target", "Cible");

		ObjectProperty hasContext = om.createObjectProperty(ncl + "hasContext");
		hasContext.addDomain(ProductArgument);
		hasContext.addRange(Context);
		hasContext.addComment("Links an argument to its context", "en");
		hasContext.addComment("Relie un argument à son contexte", "fr");
		addBilingualLabels(hasContext, prefLabel, "Has context", "A pour contexte");

		ObjectProperty hasContextProduct = om.createObjectProperty(ncl + "hasContextProduct");
		hasContextProduct.addDomain(Context);
		hasContextProduct.addRange(ContextProduct);
		hasContextProduct.addComment("The product context of an argument.", "en");
		hasContextProduct.addComment("Le contexte produit d'un argument.", "fr");
		addBilingualLabels(hasContextProduct, prefLabel, "Has product context", "A pour contexte produit");

		ObjectProperty hasContextIngredient = om.createObjectProperty(ncl + "hasContextIngredient");
		hasContextIngredient.addDomain(Context);
		hasContextIngredient.addRange(ContextIngredient);
		hasContextIngredient.addComment("The ingredient context of an argument.", "en");
		hasContextIngredient.addComment("Le contexte ingrédient d'un argument.", "fr");
		addBilingualLabels(hasContextIngredient, prefLabel, "Has ingredient context", "A pour contexte ingrédient");
		
		ObjectProperty hasVerbatim = om.createObjectProperty(ncl + "hasVerbatim");
		hasVerbatim.addDomain(ProductArgument);
		hasVerbatim.addRange(Verbatim);
		hasVerbatim.addComment("Link an argument to its verbatim.", "en");
		hasVerbatim.addComment("Link an argument to its verbatim.", "fr");
		addBilingualLabels(hasVerbatim, prefLabel, "Has verbatim", "A pour verbatim");

		ObjectProperty hasStakeholder = om.createObjectProperty(ncl + "hasStakeholder");
		hasStakeholder.addDomain(ProductArgument);
		hasStakeholder.addRange(Stakeholder);
		hasStakeholder.addComment("Link an argument to its stakeholder (consumer, manufacturer, etc.).", "en");
		hasStakeholder.addComment("Relie un argument à sa partie prenante (consommateur, fabricant, etc.).", "fr");
		addBilingualLabels(hasStakeholder, prefLabel, "Has stakeholder", "A pour partie prenante");
		
		ObjectProperty hasSource = om.createObjectProperty(ncl + "hasSource");
		hasSource.addDomain(ProductArgument);
		hasSource.addRange(Source);
		hasSource.addComment("Link an argument to its source (consumer survey, scientific journal, etc.).", "en");
		hasSource.addComment("Relie un argument à sa source (enquête consommateur, revue scientifique, etc.).", "fr");
		addBilingualLabels(hasSource, prefLabel, "Has source", "A pour source");

		ObjectProperty hasTypeSource = om.createObjectProperty(ncl + "hasTypeSource");
		hasTypeSource.addDomain(Source);
		hasTypeSource.addRange(TypeSource);
		hasTypeSource.addComment("Link a source to its type (expert, peer-reviewed article, etc.).", "en");
		hasTypeSource.addComment("Relie une source à son type (expert, article peer-reviewed, etc.).", "fr");
		addBilingualLabels(hasTypeSource, prefLabel, "Has source type", "A pour type de source");

		ObjectProperty hasAttribute = om.createObjectProperty(ncl + "hasAttribute");
		hasAttribute.addDomain(ProductArgument);
		hasAttribute.addRange(Attribute);
		hasAttribute.addComment("The naturalness attribute of a food industry product.", "en");
		hasAttribute.addComment("Attribut de la naturalité d'un produit de l'industrie agro-alimentaire.", "fr");
		addBilingualLabels(hasAttribute, prefLabel, "Has attribute", "A pour attribut");

		ObjectProperty hasCategory = om.createObjectProperty(ncl + "hasCategory");
		hasCategory.addDomain(ProductArgument);
		hasCategory.addRange(Category);
		hasCategory.addComment("Links an argument to its main category.", "en");
		hasCategory.addComment("Relie un argument à sa catégorie principale.", "fr");
		addBilingualLabels(hasCategory, prefLabel, "Has category", "A pour catégorie");

		ObjectProperty hasSubcategory = om.createObjectProperty(ncl + "hasSubcategory");
		hasSubcategory.addDomain(ProductArgument);
		hasSubcategory.addRange(Subcategory);
		hasSubcategory.addComment("Links an argument to its subcategory.", "en");
		hasSubcategory.addComment("Relie un argument à sa sous-catégorie.", "fr");
		addBilingualLabels(hasSubcategory, prefLabel, "Has subcategory", "A pour sous-catégorie");

		ObjectProperty containsIngredientWithFunction = om.createObjectProperty(ncl + "containsIngredientWithFunction");
		containsIngredientWithFunction.addDomain(Product);
		containsIngredientWithFunction.addRange(om.createResource(XSD.xstring.getURI()));
		containsIngredientWithFunction.addComment("Links a product to the function of its ingredients.", "en");
		containsIngredientWithFunction.addComment("Relie un produit à la fonction de ses ingrédients.", "fr");
		addBilingualLabels(containsIngredientWithFunction, prefLabel, "Contains ingredient with function", "Contient un ingrédient avec fonction");

		ObjectProperty hasAdditiveIngredients = om.createObjectProperty(ncl + "hasAdditiveIngredients");
		hasAdditiveIngredients.addDomain(CalculatedNOVAgroupDetails);
		hasAdditiveIngredients.addRange(Ingredient);
		hasAdditiveIngredients.addComment("Lists additive ingredients found during NOVA calculation.", "en");
		hasAdditiveIngredients.addComment("Liste les ingrédients additifs trouvés lors du calcul NOVA.", "fr");
		addBilingualLabels(hasAdditiveIngredients, prefLabel, "Has additive ingredients", "A pour ingrédients additifs");

		ObjectProperty hasTechnologicalIngredients = om.createObjectProperty(ncl + "hasTechnologicalIngredients");
		hasTechnologicalIngredients.addDomain(CalculatedNOVAgroupDetails);
		hasTechnologicalIngredients.addRange(Ingredient);
		hasTechnologicalIngredients.addComment("Lists technological ingredients found during NOVA calculation.", "en");
		hasTechnologicalIngredients.addComment("Liste les ingrédients technologiques trouvés lors du calcul NOVA.", "fr");
		addBilingualLabels(hasTechnologicalIngredients, prefLabel, "Has technological ingredients", "A pour ingrédients technologiques");

		ObjectProperty hasProcessedIngredients = om.createObjectProperty(ncl + "hasProcessedIngredients");
		hasProcessedIngredients.addDomain(CalculatedNOVAgroupDetails);
		hasProcessedIngredients.addRange(Ingredient);
		hasProcessedIngredients.addComment("Lists processed ingredients found during NOVA calculation.", "en");
		hasProcessedIngredients.addComment("Liste les ingrédients transformés trouvés lors du calcul NOVA.", "fr");
		addBilingualLabels(hasProcessedIngredients, prefLabel, "Has processed ingredients", "A pour ingrédients transformés");

		ObjectProperty hasRawIngredients = om.createObjectProperty(ncl + "hasRawIngredients");
		hasRawIngredients.addDomain(CalculatedNOVAgroupDetails);
		hasRawIngredients.addRange(Ingredient);
		hasRawIngredients.addComment("Lists raw ingredients found during NOVA calculation.", "en");
		hasRawIngredients.addComment("Liste les ingrédients bruts trouvés lors du calcul NOVA.", "fr");
		addBilingualLabels(hasRawIngredients, prefLabel, "Has raw ingredients", "A pour ingrédients bruts");


	    //////////////////////////////////////////////////////////
	    // Définition des data property                         //
	    //////////////////////////////////////////////////////////
	    DatatypeProperty quantity = om.createDatatypeProperty(ncl + "quantity");
		quantity.addDomain(QuantifiedElement);
		quantity.addRange(om.createResource(XSD.xdouble.getURI()));
		quantity.addComment("Quantity of a by-product or ingredient present in a food industry product.", "en");
		quantity.addComment("Quantité présente d'un sous-produit ou d'un ingrédient dans un produit de l'industrie agro-alimentaire.", "fr");
		addBilingualLabels(quantity, prefLabel, "Quantity", "Quantité");
		
		
		DatatypeProperty unit = om.createDatatypeProperty(ncl + "unit");
		unit.addDomain(QuantifiedElement);
		unit.addRange(om.createResource(XSD.xstring.getURI()));
		unit.addComment("Unit of the quantity of a by-product or ingredient present in a food industry product.", "en");
		unit.addComment("Unité de la quantité présente d'un sous-produit ou d'un ingrédient dans un produit de l'industrie agro-alimentaire.", "fr");
		addBilingualLabels(unit, prefLabel, "Unit", "Unité");
		
		DatatypeProperty percentage = om.createDatatypeProperty(ncl + "percentage");
		percentage.addDomain(QuantifiedElement);
		percentage.addRange(om.createResource(XSD.xdouble.getURI()));
		percentage.addComment("Percentage (by weight) of a by-product or ingredient in a food industry product.", "en");
		percentage.addComment("Pourcentage (du poids) d'un sous-produit ou d'un ingrédient dans un produit de l'industrie agro-alimentaire.", "fr");
		addBilingualLabels(percentage, prefLabel, "Percentage", "Pourcentage");
		
		
		DatatypeProperty rank = om.createDatatypeProperty(ncl + "rank");
		rank.addDomain(QuantifiedElement);
		rank.addRange(om.createResource(XSD.nonNegativeInteger.getURI()));
		rank.addComment("Classification of an ingredient or by-product in the textual statement of a product's composition.", "en");
		rank.addComment("Classement d'un ingrédient ou d'un sous-produit dans l’énoncé textuel de la composition d'un produit.", "fr");
		addBilingualLabels(rank, prefLabel, "Rank", "Rang");

		DatatypeProperty hasText = om.createDatatypeProperty(ncl + "hasText");
		hasText.addDomain(Verbatim);
		hasText.addRange(om.createResource(XSD.xstring.getURI()));
		hasText.addComment("Verbatim text.", "en");
		hasText.addComment("Texte du verbatim.", "fr");
		addBilingualLabels(hasText, prefLabel, "Has text", "A pour texte");

		DatatypeProperty supportType = om.createDatatypeProperty(ncl + "supportType");
		supportType.addDomain(ProductArgument);
		supportType.addRange(om.createResource(XSD.xstring.getURI()));
		supportType.addComment("Type of argument (positive or negative) in relation to its target.", "en");
		supportType.addComment("Type de l'argument (positif ou négatif) vis à vis de sa cible.", "fr");
		addBilingualLabels(supportType, prefLabel, "Support type", "Type de support");
		

		DatatypeProperty fiability = om.createDatatypeProperty(ncl + "fiability");
		fiability.addDomain(TypeSource);
		fiability.addRange(om.createResource(XSD.xdouble.getURI()));
		fiability.addComment("Fiability score of the source type.", "en");
		fiability.addComment("Score de fiabilité du type de source.", "fr");
		addBilingualLabels(fiability, prefLabel, "Reliability", "Fiabilité");
		

		DatatypeProperty assertion = om.createDatatypeProperty(ncl + "assertion");
		assertion.addDomain(TagArgumentBinding);
		assertion.addRange(om.createResource(XSD.xstring.getURI()));
		assertion.addComment("The assertion carried by the tag binding.", "en");
		assertion.addComment("L'assertion portée par le binding de tag.", "fr");
		addBilingualLabels(assertion, prefLabel, "Assertion", "Assertion");

		DatatypeProperty polarity = om.createDatatypeProperty(ncl + "polarity");
		polarity.addDomain(TagArgumentBinding);
		polarity.addRange(om.createResource(XSD.xstring.getURI()));
		polarity.addComment("Polarity of the tag binding (positive '+' or negative '-').", "en");
		polarity.addComment("Polarité du binding de tag (positif '+' ou négatif '-').", "fr");
		addBilingualLabels(polarity, prefLabel, "Polarity", "Polarité");

		DatatypeProperty nameCriterion = om.createDatatypeProperty(ncl + "nameCriterion");
		nameCriterion.addDomain(TagArgumentBinding);
		nameCriterion.addRange(om.createResource(XSD.xstring.getURI()));
		nameCriterion.addComment("Name of the criterion addressed by the tag binding.", "en");
		nameCriterion.addComment("Nom du critère abordé par le binding de tag.", "fr");
		addBilingualLabels(nameCriterion, prefLabel, "Criterion name", "Nom du critère");

		DatatypeProperty aim = om.createDatatypeProperty(ncl + "aim");
		aim.addDomain(TagArgumentBinding);
		aim.addRange(om.createResource(XSD.xstring.getURI()));
		aim.addComment("Aim or objective related to the tag binding.", "en");
		aim.addComment("Objectif ou visée du binding de tag.", "fr");
		addBilingualLabels(aim, prefLabel, "Aim", "Objectif");

		DatatypeProperty nameProperty = om.createDatatypeProperty(ncl + "nameProperty");
		nameProperty.addDomain(TagArgumentBinding);
		nameProperty.addRange(om.createResource(XSD.xstring.getURI()));
		nameProperty.addComment("Name of the property evaluated in the tag binding.", "en");
		nameProperty.addComment("Nom de la propriété évaluée dans le binding de tag.", "fr");
		addBilingualLabels(nameProperty, prefLabel, "Property name", "Nom de la propriété");

		DatatypeProperty valueProperty = om.createDatatypeProperty(ncl + "valueProperty");
		valueProperty.addDomain(TagArgumentBinding);
		valueProperty.addRange(om.createResource(XSD.xstring.getURI()));
		valueProperty.addComment("Value associated with the tag binding property.", "en");
		valueProperty.addComment("Valeur associée à la propriété du binding de tag.", "fr");
		addBilingualLabels(valueProperty, prefLabel, "Property value", "Valeur de la propriété");

		DatatypeProperty bindingKeywords = om.createDatatypeProperty(ncl + "bindingKeywords");
		bindingKeywords.addDomain(TagArgumentBinding);
		bindingKeywords.addRange(om.createResource(XSD.xstring.getURI()));
		bindingKeywords.addComment("Keywords used to bind a tag to argumentation context.", "en");
		bindingKeywords.addComment("Mots-clés utilisés pour lier un tag au contexte d'argumentation.", "fr");
		addBilingualLabels(bindingKeywords, prefLabel, "Binding keywords", "Mots-clés de liaison");

		DatatypeProperty condition = om.createDatatypeProperty(ncl + "condition");
		condition.addDomain(ProductArgument);
		condition.addRange(om.createResource(XSD.xstring.getURI()));
		condition.addComment("Condition under which the argument applies.", "en");
		condition.addComment("Condition dans laquelle l'argument s'applique.", "fr");
		addBilingualLabels(condition, prefLabel, "Condition", "Condition");

		DatatypeProperty infValue = om.createDatatypeProperty(ncl + "infValue");
		infValue.addDomain(ProductArgument);
		infValue.addRange(om.createResource(XSD.xdouble.getURI()));
		infValue.addComment("Lower bound value for the argument condition.", "en");
		infValue.addComment("Valeur minimale pour la condition de l'argument.", "fr");
		addBilingualLabels(infValue, prefLabel, "Lower bound value", "Valeur inférieure");

		DatatypeProperty supValue = om.createDatatypeProperty(ncl + "supValue");
		supValue.addDomain(ProductArgument);
		supValue.addRange(om.createResource(XSD.xdouble.getURI()));
		supValue.addComment("Upper bound value for the argument condition.", "en");
		supValue.addComment("Valeur maximale pour la condition de l'argument.", "fr");
		addBilingualLabels(supValue, prefLabel, "Upper bound value", "Valeur supérieure");

	DatatypeProperty unitArg = om.createDatatypeProperty(ncl + "unitArg");
	unitArg.addDomain(ProductArgument);
	unitArg.addRange(om.createResource(XSD.xstring.getURI()));
		unitArg.addComment("Unit of measurement for the argument values.", "en");
		unitArg.addComment("Unité de mesure pour les valeurs de l'argument.", "fr");
		addBilingualLabels(unitArg, prefLabel, "Argument unit", "Unité de l'argument");

		DatatypeProperty hasCiqualFoodCode = om.createDatatypeProperty(ncl + "hasCiqualFoodCode");
		hasCiqualFoodCode.addDomain(Ingredient);
		hasCiqualFoodCode.addRange(om.createResource(XSD.xstring.getURI()));
		hasCiqualFoodCode.addComment("Ciqual code for a food industry product.", "en");
		hasCiqualFoodCode.addComment("Code Ciqual d'un produit de l'industrie agroalimentaire.", "fr");
		addBilingualLabels(hasCiqualFoodCode, prefLabel, "Has Ciqual food code", "A pour code Ciqual");
		
		DatatypeProperty hasCiqualProxyFoodCode = om.createDatatypeProperty(ncl + "hasCiqualProxyFoodCode");
		hasCiqualProxyFoodCode.addDomain(Ingredient);
		hasCiqualProxyFoodCode.addRange(om.createResource(XSD.xstring.getURI()));
		hasCiqualProxyFoodCode.addComment("Ciqual code for a similar food industry product.", "en");
		hasCiqualProxyFoodCode.addComment("Code Ciqual d'un produit similaire de l'industrie agroalimentaire.", "fr");
		addBilingualLabels(hasCiqualProxyFoodCode, prefLabel, "Has Ciqual proxy food code", "A pour code Ciqual proxy");
		
		ObjectProperty hasRole = om.createObjectProperty(ncl + "hasRole");
		hasRole.addDomain(Ingredient);
		hasRole.addRange(om.createResource(ncl + "Tag"));
		hasRole.addComment("Links an ingredient to a Tag resource representing its technological or sensory function (preservative, flavor enhancer, etc.).", "en");
		hasRole.addComment("Relie un ingrédient à une ressource Tag représentant sa fonction technologique ou sensorielle (conservateur, exhausteur de goût, etc.).", "fr");
		addBilingualLabels(hasRole, prefLabel, "Has role", "A pour rôle");

		DatatypeProperty containsAdditives = om.createDatatypeProperty(ncl + "containsAdditives");
		containsAdditives.addDomain(Product);
		containsAdditives.addRange(om.createResource(XSD.xboolean.getURI()));
		containsAdditives.addComment("Indicates whether the product contains additives.", "en");
		containsAdditives.addComment("Indique si le produit contient des additifs.", "fr");			
		addBilingualLabels(containsAdditives, prefLabel, "Contains additives", "Contient des additifs");

		// Calculated NOVA group (1..4) stored on Product when derived locally
		DatatypeProperty hasCalculatedNOVAgroup = om.createDatatypeProperty(ncl + "hasCalculatedNOVAgroup");
		hasCalculatedNOVAgroup.addDomain(Product);
		hasCalculatedNOVAgroup.addRange(om.createResource(XSD.nonNegativeInteger.getURI()));
		hasCalculatedNOVAgroup.addComment("Calculated NOVA group (1..4) inferred by rules, not the OFF-provided value.", "en");
		hasCalculatedNOVAgroup.addComment("Groupe NOVA calculé (1..4) inféré par les règles, distinct de la valeur fournie par OFF.", "fr");
		addBilingualLabels(hasCalculatedNOVAgroup, prefLabel, "Has calculated NOVA group", "A pour groupe NOVA calculé");

		// OFF-provided NOVA group (1..4), dedicated property to store exact OFF value
		DatatypeProperty hasNOVAgroup = om.createDatatypeProperty(ncl + "hasNOVAgroup");
		hasNOVAgroup.addDomain(Product);
		hasNOVAgroup.addRange(om.createResource(XSD.nonNegativeInteger.getURI()));
		hasNOVAgroup.addComment("NOVA group (1..4) as provided by Open Food Facts.", "en");
		hasNOVAgroup.addComment("Groupe NOVA (1..4) tel que fourni par Open Food Facts.", "fr");
		addBilingualLabels(hasNOVAgroup, prefLabel, "Has NOVA group", "A pour groupe NOVA");

		DatatypeProperty groupe1 = om.createDatatypeProperty(ncl + "groupe1");
		groupe1.addDomain(NOVAgroupDetails);
		groupe1.addRange(om.createResource(XSD.xstring.getURI()));
		groupe1.addComment("NOVA group 1 characteristics (unprocessed or minimally processed foods).", "en");
		groupe1.addComment("Caractéristiques du groupe NOVA 1 (aliments non transformés ou minimalement transformés).", "fr");
		addBilingualLabels(groupe1, prefLabel, "Group 1", "Groupe 1");

		DatatypeProperty groupe2 = om.createDatatypeProperty(ncl + "groupe2");
		groupe2.addDomain(NOVAgroupDetails);
		groupe2.addRange(om.createResource(XSD.xstring.getURI()));
		groupe2.addComment("NOVA group 2 characteristics (processed culinary ingredients).", "en");
		groupe2.addComment("Caractéristiques du groupe NOVA 2 (ingrédients culinaires transformés).", "fr");
		addBilingualLabels(groupe2, prefLabel, "Group 2", "Groupe 2");

		DatatypeProperty groupe3 = om.createDatatypeProperty(ncl + "groupe3");
		groupe3.addDomain(NOVAgroupDetails);
		groupe3.addRange(om.createResource(XSD.xstring.getURI()));
		groupe3.addComment("NOVA group 3 characteristics (processed foods).", "en");
		groupe3.addComment("Caractéristiques du groupe NOVA 3 (aliments transformés).", "fr");
		addBilingualLabels(groupe3, prefLabel, "Group 3", "Groupe 3");

		DatatypeProperty groupe4 = om.createDatatypeProperty(ncl + "groupe4");
		groupe4.addDomain(NOVAgroupDetails);
		groupe4.addRange(om.createResource(XSD.xstring.getURI()));
		groupe4.addComment("NOVA group 4 characteristics (ultra-processed foods and drinks).", "en");
		groupe4.addComment("Caractéristiques du groupe NOVA 4 (produits ultra-transformés).", "fr");
		addBilingualLabels(groupe4, prefLabel, "Group 4", "Groupe 4");

		//////////////////////////////////////////////////////////
	    // Définition des annotation property                   //
	    //////////////////////////////////////////////////////////
		om.createAnnotationProperty(ncl + "hasEAN13");
		om.createAnnotationProperty(ncl + "hasTrademark");
		om.createAnnotationProperty(ncl + "hasIdIngredientOFF");
		om.createAnnotationProperty(skos + "prefLabel");
	    om.createAnnotationProperty(skos + "altLabel");
		om.createAnnotationProperty(skos + "definition");
		om.createAnnotationProperty(rdfs + "label");
		om.createAnnotationProperty(rdfs + "comment");
		om.createAnnotationProperty(dct + "created");




		
	//////////////////////////////////////////////////////////
	// Inclusion/Equivalence de concepts                    //
	//////////////////////////////////////////////////////////


	/////////////////////////////
	// Inclusion de concepts   //
	/////////////////////////////
	
	NCL.addSubClass(Resource);
	NCL.addSubClass(ProductArgument);
	NCL.addSubClass(CleanLabel);
	NCL.addSubClass(Context);
	// NCL.addSubClass(ContextIngredient);
	// NCL.addSubClass(ContextProduct);
	NCL.addSubClass(ControlledOriginLabel);
	NCL.addSubClass(FNI);
	NCL.addSubClass(NutriScore);
	NCL.addSubClass(NutriScoreDetail);
	NCL.addSubClass(NutriScoreAlpha);
	NCL.addSubClass(NOVAgroupDetails);
	NCL.addSubClass(CalculatedNOVAgroupDetails);
	NCL.addSubClass(Packaging);
	NCL.addSubClass(Shape);
	NCL.addSubClass(Material);
	NCL.addSubClass(QuantifiedElement);	
	NCL.addSubClass(Source);
	NCL.addSubClass(Verbatim);
	NCL.addSubClass(ManufacturingProcess);
	NCL.addSubClass(NaturalnessScore);
	NCL.addSubClass(Allegation);
	NCL.addSubClass(Origin);
	NCL.addSubClass(Attribute);
	NCL.addSubClass(Category);
	NCL.addSubClass(Subcategory);
	NCL.addSubClass(Stakeholder);
	NCL.addSubClass(LinkToArgument);
	NCL.addSubClass(Tag);
	NCL.addSubClass(TagArgumentBinding);
	NCL.addSubClass(TypeSource);

	
	CompositeProduct.addSuperClass(Product);
	SimpleProduct.addSuperClass(Product);
	Product.addSuperClass(Resource);
	ProductArgument.addSuperClass(Node);
	Ingredient.addSuperClass(Resource);
		CompositeIngredient.addSuperClass(Ingredient);
		SimpleIngredient.addSuperClass(Ingredient);
		// Sous-classes directes de Ingredient
		IngredientByOrigin.addSuperClass(Ingredient);
		IngredientByFunction.addSuperClass(Ingredient);
		IngredientByTransformationDegree.addSuperClass(Ingredient);

		// Origine (sous IngredientByOrigin)
		PlantOriginIngredient.addSuperClass(IngredientByOrigin);
		AnimalOriginIngredient.addSuperClass(IngredientByOrigin);
		MineralOriginIngredient.addSuperClass(IngredientByOrigin);
		FungalOrMicrobialIngredient.addSuperClass(IngredientByOrigin);
		SyntheticOrBiotechIngredient.addSuperClass(IngredientByOrigin);

		// Fonction (sous IngredientByFunction)
		MainComponentIngredient.addSuperClass(IngredientByFunction);
		AdditiveIngredient.addSuperClass(IngredientByFunction);
		FlavorIngredient.addSuperClass(IngredientByFunction);
		EnzymeIngredient.addSuperClass(IngredientByFunction);
		FunctionalNutrientIngredient.addSuperClass(IngredientByFunction);
		TechnologicalIngredient.addSuperClass(IngredientByFunction);

		// Degré de transformation (sous IngredientByTransformationDegree)
		RawIngredient.addSuperClass(IngredientByTransformationDegree);
		ProcessedIngredient.addSuperClass(IngredientByTransformationDegree);
		UltraProcessedIngredient.addSuperClass(IngredientByTransformationDegree);

		// exporte le resultat dans un fichier au format RDF/JSON
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		RDFDataMgr.write(out, om, RDFFormat.JSONLD11);
		try {
			jsonString = out.toString("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return jsonString;
	}

	private static void addBilingualLabels(Resource resource, Property prefLabel, String englishLabel, String frenchLabel) {
		if (!resource.hasProperty(RDFS.label, englishLabel, "en")) {
			resource.addProperty(RDFS.label, englishLabel, "en");
		}
		if (!resource.hasProperty(RDFS.label, frenchLabel, "fr")) {
			resource.addProperty(RDFS.label, frenchLabel, "fr");
		}
		if (!resource.hasProperty(prefLabel, englishLabel, "en")) {
			resource.addProperty(prefLabel, englishLabel, "en");
		}
		if (!resource.hasProperty(prefLabel, frenchLabel, "fr")) {
			resource.addProperty(prefLabel, frenchLabel, "fr");
		}
	}
}