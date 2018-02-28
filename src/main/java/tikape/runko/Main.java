package tikape.runko;


import java.util.HashMap;
import java.util.List;
import spark.ModelAndView;
import spark.Spark;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.AnnosDao;
import tikape.runko.database.Database;
import tikape.runko.database.RaakaAineDao;
import tikape.runko.domain.Annos;
import tikape.runko.domain.RaakaAine;
import tikape.runko.database.AnnosRaakaAineDao;
import tikape.runko.domain.AnnosRaakaAine;

public class Main {
   
    
    public static void main(String[] args) throws Exception {
        
        
        // asetetaan portti jos heroku antaa PORT-ympäristömuuttujan
        if (System.getenv("PORT") != null) {
            Spark.port(Integer.valueOf(System.getenv("PORT")));
        }
        
        Database database = new Database("jdbc:sqlite:db/smoothie.db");
        database.init();

        RaakaAineDao raakaAineDao = new RaakaAineDao(database);
        AnnosDao annosDao = new AnnosDao(database);
        AnnosRaakaAineDao annosRaakaAineDao = new AnnosRaakaAineDao(database);
        
        
        // index.html:
        Spark.get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("annokset", annosDao.findAll());

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());
        
        Spark.get("/smoothiet", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("annokset", annosDao.findAll());
            map.put("ainekset", raakaAineDao.findAll());

            return new ModelAndView(map, "smoothiet");
        }, new ThymeleafTemplateEngine());
        
        Spark.get("/ainekset", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("ainekset", raakaAineDao.findAll());

            return new ModelAndView(map, "ainekset");
        }, new ThymeleafTemplateEngine());
        
        Spark.get("/ainekset/:id/delete", (req, res) -> {
            HashMap map = new HashMap<>();
            Integer raakaAineId = Integer.parseInt(req.params(":id"));
            RaakaAine raakaAine = raakaAineDao.findOne(raakaAineId);
            
            // Delete raakaAine 
            raakaAineDao.delete(raakaAineId);
            annosRaakaAineDao.deleteRaakaAine(raakaAineId);
            
            map.put("id", raakaAineId);
            map.put("deleted", raakaAine.getNimi());
                    
            return new ModelAndView(map, "delete");
        }, new ThymeleafTemplateEngine());
        
        
        Spark.get("/smoothiet/:id/delete", (req, res) -> {
            HashMap map = new HashMap<>();
            Integer annosId = Integer.parseInt(req.params(":id"));
            Annos annos = annosDao.findOne(annosId);
            
            
            annosDao.delete(annosId); // from AnnosDao
            annosRaakaAineDao.deleteAnnos(annosId); // RaakaAineDao
            
            map.put("id", annosId);
            map.put("removed", annos.getNimi());
                    
            return new ModelAndView(map, "delete");
        }, new ThymeleafTemplateEngine());
        
        
        // Create spesific Annos:
        Spark.get("/smoothiet/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            
            // Define Annos and its id:
            Integer annosId = Integer.parseInt(req.params(":id"));
            Annos annos = annosDao.findOne(annosId);
            
            // Find raakaAine list:
            List<RaakaAine> raakaAineet = raakaAineDao.findRaakaAineByAnnos(annosId);
            
            map.put("annosId", annosId);
            map.put("annos", annos);
            map.put("raakaAineet", raakaAineet);
            map.put("annosRaakaAineet", annosRaakaAineDao.findRaakaAineByAnnos(annosId));
            
            return new ModelAndView(map, "annos");
        },  new ThymeleafTemplateEngine());
        
        Spark.post("/ainekset", (req, res) -> {
            RaakaAine r = new RaakaAine(-1, req.queryParams("nimi"));
            raakaAineDao.saveOrUpdate(r);
            
            res.redirect("/ainekset");
            return "";
        });
        
        // Create new smoothie
        
        Spark.post("/smoothiet/annos", (req, res) -> {
            Annos a = new Annos(-1, req.queryParams("nimi"));
            annosDao.saveOrUpdate(a);
            
            res.redirect("/smoothiet");
            return "";
        });
        
        // AnnosRaakaAine
        
        Spark.post("/smoothiet/annosraakaaine", (req, res) -> {
            Integer annosId = Integer.parseInt(req.queryParams("smoothieId"));
            Integer raakaAineId = Integer.parseInt(req.queryParams("raakaAineId"));
            
            double maara = Double.parseDouble(req.queryParams("maara"));
            Integer jarjestys = Integer.parseInt(req.queryParams("jarjestys"));
            String ohje = req.queryParams("ohje");
            
            AnnosRaakaAine annosRaakaAine = new AnnosRaakaAine(-1 ,annosId, raakaAineId, jarjestys, maara, ohje);
            annosRaakaAineDao.saveOrUpdate(annosRaakaAine);
           
            res.redirect("/smoothiet");
            return "";
        });
    }
}



/*
import java.util.HashMap;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.Database;
import tikape.runko.database.OpiskelijaDao;

public class Main {

    public static void main(String[] args) throws Exception {
        Database database = new Database("jdbc:sqlite:opiskelijat.db");
        database.init();

        OpiskelijaDao opiskelijaDao = new OpiskelijaDao(database);

        get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("viesti", "tervehdys");

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        get("/opiskelijat", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("opiskelijat", opiskelijaDao.findAll());

            return new ModelAndView(map, "opiskelijat");
        }, new ThymeleafTemplateEngine());

        get("/opiskelijat/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("opiskelija", opiskelijaDao.findOne(Integer.parseInt(req.params("id"))));

            return new ModelAndView(map, "opiskelija");
        }, new ThymeleafTemplateEngine());
    }
}
*/