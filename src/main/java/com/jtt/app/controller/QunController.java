package com.jtt.app.controller;

import com.jtt.app.common.ServerResponse;
import com.jtt.app.dao.IQunMapper;
import com.jtt.app.model.Qun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author : zengshuaizhi
 * @date : 2019-06-30 21:26
 */
@RestController
@RequestMapping("/qun")
public class QunController {

    @Autowired
    private IQunMapper qunMapper;

    @GetMapping("/addQun/{qunId}")
    public ServerResponse<String> addQun(@RequestParam Long uid, @PathVariable Long qunId) {
        int result = qunMapper.insertNewQunRelation(uid, qunId);
        if (result > 0) {
            qunMapper.updateQunCount(result, qunId);
            return ServerResponse.createBySuccessMsg("添加群成功");
        }

        return ServerResponse.createByErrorMsg("添加群失败");
    }

    @GetMapping("/info/{qunId}")
    public ServerResponse<Qun> getQunInfo(@PathVariable Long qunId) {
        Qun qun = qunMapper.getQunInfo(qunId);
        if (qun == null) {
            return ServerResponse.createByErrorMsg("该群不存在");
        }
        return ServerResponse.createBySuccess("查询成功", qun);
    }

    @GetMapping("/list")
    public ServerResponse<List<Qun>> getQunList() {
        List<Qun> quns = qunMapper.getQunList();
        if (CollectionUtils.isEmpty(quns)) {
            return ServerResponse.createByErrorMsg("系统暂无群信息");
        }

        return ServerResponse.createBySuccess("查询成功", quns);
    }
}
