<script setup>
import { ref } from 'vue'
import { userPasswordUpdateService } from '@/api/user.js'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { useTokenStore } from '@/stores/token.js'
import useUserInfoStore from '@/stores/userInfo.js'

const formRef = ref()
const router = useRouter()
const tokenStore = useTokenStore()
const userInfoStore = useUserInfoStore()

// 数据模型
const passwordForm = ref({
    old_pwd: '',
    new_pwd: '',
    re_pwd: ''
})

// 校验规则
const checkRePassword = (rule, value, callback) => {
    if (value === '') {
        callback(new Error('请再次确认新密码'))
    } else if (value !== passwordForm.value.new_pwd) {
        callback(new Error('两次输入的密码不一致'))
    } else {
        callback()
    }
}

const rules = {
    old_pwd: [
        { required: true, message: '请输入旧密码', trigger: 'blur' },
        { min: 5, max: 16, message: '长度为5~16位非空字符', trigger: 'blur' }
    ],
    new_pwd: [
        { required: true, message: '请输入新密码', trigger: 'blur' },
        { min: 5, max: 16, message: '长度为5~16位非空字符', trigger: 'blur' }
    ],
    re_pwd: [
        { required: true, validator: checkRePassword, trigger: 'blur' }
    ]
}

// 提交修改
const updatePassword = async () => {
    // 1. 预校验
    await formRef.value.validate()
    
    // 2. 调用接口
    await userPasswordUpdateService(passwordForm.value)
    
    ElMessage.success('密码修改成功，请重新登录')
    
    // 3. 清空本地存储的 token 和用户信息
    tokenStore.removeToken()
    userInfoStore.removeInfo()
    
    // 4. 跳转登录页
    router.push('/login')
}

// 重置表单
const resetForm = () => {
    formRef.value.resetFields()
}
</script>

<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>重置密码</span>
            </div>
        </template>
        <el-row>
            <el-col :span="12">
                <el-form ref="formRef" :model="passwordForm" :rules="rules" label-width="100px" size="large">
                    <el-form-item label="旧密码" prop="old_pwd">
                        <el-input v-model="passwordForm.old_pwd" type="password" show-password placeholder="请输入旧密码"></el-input>
                    </el-form-item>
                    <el-form-item label="新密码" prop="new_pwd">
                        <el-input v-model="passwordForm.new_pwd" type="password" show-password placeholder="请输入新密码"></el-input>
                    </el-form-item>
                    <el-form-item label="确认新密码" prop="re_pwd">
                        <el-input v-model="passwordForm.re_pwd" type="password" show-password placeholder="请再次输入新密码"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="updatePassword">提交修改</el-button>
                        <el-button @click="resetForm">重置</el-button>
                    </el-form-item>
                </el-form>
            </el-col>
        </el-row>
    </el-card>
</template>

<style lang="scss" scoped>
.page-container {
    min-height: 100%;
    box-sizing: border-box;
    /* 视觉优化：圆角与阴影 */
    border: none !important;
    border-radius: 12px;
    box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08) !important;

    .header {
        display: flex;
        align-items: center;
        justify-content: space-between;
    }
}
</style>