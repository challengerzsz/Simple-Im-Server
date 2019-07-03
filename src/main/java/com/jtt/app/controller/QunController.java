package com.jtt.app.controller;

import com.jtt.app.common.ServerResponse;
import com.jtt.app.dao.IQunMapper;
import com.jtt.app.model.Qun;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author : zengshuaizhi
 * @date : 2019-06-30 21:26
 */
@RestController
@RequestMapping("/qun")
public class QunController {

    public static final String INVITED_UID_LIST_KEY = "uids";

    public static final String QUN_NAME_KEY = "qunName";

    @Autowired
    private IQunMapper qunMapper;

    @PostMapping("/createQun")
    @Transactional
    public ServerResponse<String> createQun(@RequestBody Map<String, Object> json) {
        String qunName = (String) json.get(QUN_NAME_KEY);
        if (StringUtils.isBlank(qunName)) {
            return ServerResponse.createByErrorMsg("群名不能为空");
        }

        Qun newQun = new Qun();
        newQun.setName(qunName);
        int result = qunMapper.createQun(newQun);
        if (result <= 0) {
            return ServerResponse.createByErrorMsg("创建群失败");
        }

        List<Long> uids = (List<Long>) json.get(INVITED_UID_LIST_KEY);

        qunMapper.insertBatchNewQunRelation(uids, newQun.getId());
        qunMapper.updateQunCount(uids.size(), newQun.getId());

        return ServerResponse.createBySuccessMsg("创建群成功");
    }


    @GetMapping("/getJoinedQun/{uid}")
    public ServerResponse<List<Qun>> getJoinedQun(@PathVariable Long uid) {

        List<Qun> joinedQun = qunMapper.getJoinedQun(uid);
        if (CollectionUtils.isEmpty(joinedQun)) {
            return ServerResponse.createByErrorMsg("用户未加入群聊");
        }

        return ServerResponse.createBySuccess("查询成功", joinedQun);
    }

    @GetMapping("/addQun/{uid}/{qunId}")
    public ServerResponse<String> addQun(@PathVariable Long uid, @PathVariable Long qunId) {
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
