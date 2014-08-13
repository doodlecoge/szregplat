package szregplat.be;

import szregplat.dbcore.dao.BlockHistoryDao;
import szregplat.dbcore.dao.TheDao;
import szregplat.model.be.BlockHistory;
import szregplat.model.be.User;
import net.sf.cglib.core.Block;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by huaiwang on 14-3-29.
 */
@Controller
@RequestMapping(value = "/")
public class HomeAction {
    @Autowired
    private TheDao theDao;

    @Autowired
    private BlockHistoryDao blockHistoryDao;

    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        theDao.addHospital();
        List hospitals = theDao.getHospitals();

        List<User> users = theDao.getUsers();
        for (User user : users) {
            System.out.println("=========" + user);
        }

        List<BlockHistory> blockHistories = blockHistoryDao.getBlockHistories("321");
        for (BlockHistory blockHistory : blockHistories) {
            System.out.println("++++++++" + blockHistory.getReason());
        }
        return "index";
    }
}
