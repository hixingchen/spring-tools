<template>
  <div class="windows-commands">
    <h2>Windows 常见命令</h2>
    <el-input
      v-model="search"
      placeholder="搜索命令名称、说明或示例..."
      clearable
      prefix-icon="Search"
      size="large"
      class="search-input"
    />
    <el-collapse v-model="activeNames">
      <el-collapse-item
        v-for="category in filteredCategories"
        :key="category.name"
        :title="`${category.name}（${category.commands.length}）`"
        :name="category.name"
      >
        <el-table :data="category.commands" stripe border style="width: 100%">
          <el-table-column prop="name" label="命令" width="150" />
          <el-table-column prop="syntax" label="语法" min-width="220" />
          <el-table-column prop="desc" label="说明" min-width="200" />
          <el-table-column prop="example" label="常用示例" min-width="260" />
        </el-table>
      </el-collapse-item>
    </el-collapse>
    <el-empty v-if="filteredCategories.length === 0" description="没有匹配的命令" />
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'

const search = ref('')
const activeNames = ref([])

const categories = [
  {
    name: '文件操作',
    commands: [
      { name: 'dir', syntax: 'dir [路径] [参数]', desc: '列出目录内容', example: 'dir /a' },
      { name: 'copy', syntax: 'copy 源 目标', desc: '复制文件', example: 'copy file1.txt file2.txt' },
      { name: 'xcopy', syntax: 'xcopy 源 目标 [参数]', desc: '复制文件和目录树', example: 'xcopy src dest /s /e' },
      { name: 'robocopy', syntax: 'robocopy 源 目标 [参数]', desc: '可靠的文件复制工具', example: 'robocopy C:\\src D:\\dst /mir' },
      { name: 'move', syntax: 'move 源 目标', desc: '移动或重命名文件', example: 'move old.txt new.txt' },
      { name: 'del', syntax: 'del [参数] 文件', desc: '删除一个或多个文件', example: 'del /f /q *.tmp' },
      { name: 'ren', syntax: 'ren 旧名 新名', desc: '重命名文件', example: 'ren report.txt report_old.txt' },
      { name: 'type', syntax: 'type 文件名', desc: '显示文本文件内容', example: 'type config.ini' },
      { name: 'more', syntax: 'more < 文件名', desc: '分页显示文件内容', example: 'more < largefile.txt' },
      { name: 'findstr', syntax: 'findstr [参数] 字符串 文件', desc: '在文件中搜索字符串', example: 'findstr /i "error" log.txt' },
      { name: 'fc', syntax: 'fc 文件1 文件2', desc: '比较两个文件的差异', example: 'fc old.txt new.txt' },
      { name: 'attrib', syntax: 'attrib [属性] 文件', desc: '查看或修改文件属性', example: 'attrib +r file.txt' },
    ]
  },
  {
    name: '目录操作',
    commands: [
      { name: 'cd', syntax: 'cd [路径]', desc: '切换当前目录', example: 'cd /d D:\\work' },
      { name: 'md / mkdir', syntax: 'md 目录名', desc: '创建目录', example: 'md projects\\app' },
      { name: 'rd / rmdir', syntax: 'rd [参数] 目录名', desc: '删除目录', example: 'rd /s /q temp' },
      { name: 'tree', syntax: 'tree [路径] [参数]', desc: '以树形显示目录结构', example: 'tree /f' },
      { name: 'pushd / popd', syntax: 'pushd 路径', desc: '保存并切换目录', example: 'pushd D:\\temp' },
      { name: 'where', syntax: 'where 文件名', desc: '搜索文件所在路径', example: 'where python.exe' },
    ]
  },
  {
    name: '网络命令',
    commands: [
      { name: 'ipconfig', syntax: 'ipconfig [/all]', desc: '显示网络配置信息', example: 'ipconfig /all' },
      { name: 'ping', syntax: 'ping 目标', desc: '测试网络连通性', example: 'ping -t google.com' },
      { name: 'tracert', syntax: 'tracert 目标', desc: '跟踪路由路径', example: 'tracert google.com' },
      { name: 'nslookup', syntax: 'nslookup 域名', desc: '查询 DNS 记录', example: 'nslookup google.com' },
      { name: 'netstat', syntax: 'netstat [参数]', desc: '显示网络连接和端口', example: 'netstat -ano' },
      { name: 'netsh', syntax: 'netsh 命令', desc: '网络配置工具', example: 'netsh wlan show profiles' },
      { name: 'arp', syntax: 'arp -a', desc: '显示 ARP 缓存表', example: 'arp -a' },
      { name: 'telnet', syntax: 'telnet 主机 端口', desc: '远程连接测试', example: 'telnet 192.168.1.1 80' },
      { name: 'curl', syntax: 'curl [参数] URL', desc: '发送 HTTP 请求', example: 'curl -O https://example.com/file' },
      { name: 'pathping', syntax: 'pathping 目标', desc: '结合 ping 和 tracert 的路由追踪', example: 'pathping google.com' },
    ]
  },
  {
    name: '系统信息',
    commands: [
      { name: 'systeminfo', syntax: 'systeminfo', desc: '显示系统详细配置信息', example: 'systeminfo' },
      { name: 'hostname', syntax: 'hostname', desc: '显示计算机名称', example: 'hostname' },
      { name: 'ver', syntax: 'ver', desc: '显示 Windows 版本号', example: 'ver' },
      { name: 'whoami', syntax: 'whoami', desc: '显示当前用户信息', example: 'whoami /all' },
      { name: 'tasklist', syntax: 'tasklist [参数]', desc: '列出正在运行的进程', example: 'tasklist /fi "imagename eq chrome*"' },
      { name: 'driverquery', syntax: 'driverquery', desc: '列出已安装的驱动程序', example: 'driverquery /v' },
      { name: 'wmic', syntax: 'wmic 对象 命令', desc: 'WMI 命令行工具', example: 'wmic bios get serialnumber' },
      { name: 'getmac', syntax: 'getmac', desc: '显示网卡 MAC 地址', example: 'getmac /v' },
    ]
  },
  {
    name: '进程管理',
    commands: [
      { name: 'tasklist', syntax: 'tasklist [参数]', desc: '列出正在运行的进程', example: 'tasklist /v' },
      { name: 'taskkill', syntax: 'taskkill /pid PID 或 /im 名称', desc: '终止进程', example: 'taskkill /f /im notepad.exe' },
      { name: 'start', syntax: 'start [参数] 程序', desc: '启动程序或窗口', example: 'start notepad.exe' },
      { name: 'wmic process', syntax: 'wmic process list', desc: '列出进程详细信息', example: 'wmic process where "name=chrome.exe" get processid' },
    ]
  },
  {
    name: '服务管理',
    commands: [
      { name: 'sc', syntax: 'sc 查询/启动/停止 服务名', desc: '服务控制管理器', example: 'sc query wuauserv' },
      { name: 'net start', syntax: 'net start 服务名', desc: '启动服务', example: 'net start spooler' },
      { name: 'net stop', syntax: 'net stop 服务名', desc: '停止服务', example: 'net stop wuauserv' },
      { name: 'wmic service', syntax: 'wmic service list brief', desc: '列出所有服务', example: 'wmic service where state="running" get name' },
    ]
  },
  {
    name: '磁盘管理',
    commands: [
      { name: 'chkdsk', syntax: 'chkdsk [卷标] [参数]', desc: '检查磁盘并修复错误', example: 'chkdsk C: /f' },
      { name: 'diskpart', syntax: 'diskpart', desc: '磁盘分区管理工具', example: 'diskpart' },
      { name: 'format', syntax: 'format 卷标 [参数]', desc: '格式化磁盘', example: 'format D: /fs:NTFS' },
      { name: 'defrag', syntax: 'defrag 卷标 [参数]', desc: '磁盘碎片整理', example: 'defrag C: /O' },
      { name: 'cleanmgr', syntax: 'cleanmgr', desc: '磁盘清理工具', example: 'cleanmgr /sageset:1' },
      { name: 'vol', syntax: 'vol [卷标]', desc: '显示磁盘卷标和序列号', example: 'vol C:' },
    ]
  },
  {
    name: '环境变量',
    commands: [
      { name: 'set', syntax: 'set [变量=值]', desc: '显示或设置环境变量', example: 'set PATH' },
      { name: 'setx', syntax: 'setx 变量 值', desc: '永久设置环境变量', example: 'setx JAVA_HOME "C:\\Java"' },
      { name: 'echo %VAR%', syntax: 'echo %变量名%', desc: '显示变量值', example: 'echo %PATH%' },
      { name: 'path', syntax: 'path [新路径]', desc: '显示或设置 PATH 变量', example: 'path' },
      { name: 'prompt', syntax: 'prompt [文本]', desc: '更改命令提示符样式', example: 'prompt $P$G' },
    ]
  },
  {
    name: 'PowerShell 专用',
    commands: [
      { name: 'Get-Process', syntax: 'Get-Process [名称]', desc: '获取进程信息', example: 'Get-Process -Name chrome' },
      { name: 'Get-Service', syntax: 'Get-Service [名称]', desc: '获取服务信息', example: 'Get-Service | Where-Object {$_.Status -eq "Running"}' },
      { name: 'Get-ChildItem', syntax: 'Get-ChildItem [路径]', desc: '列出目录内容（类似 dir）', example: 'Get-ChildItem -Recurse -Filter *.log' },
      { name: 'Get-Content', syntax: 'Get-Content 文件', desc: '读取文件内容', example: 'Get-Content log.txt -Tail 50' },
      { name: 'Select-String', syntax: 'Select-String -Path 文件 -Pattern 模式', desc: '搜索文件中的文本', example: 'Select-String -Path *.log -Pattern "error"' },
      { name: 'Invoke-WebRequest', syntax: 'Invoke-WebRequest URL', desc: '发送 HTTP 请求', example: '(Invoke-WebRequest url).Content' },
      { name: 'Get-EventLog', syntax: 'Get-EventLog -LogName 名称', desc: '读取事件日志', example: 'Get-EventLog -LogName System -Newest 10' },
      { name: 'Export-Csv', syntax: '| Export-Csv 文件', desc: '将输出导出为 CSV 文件', example: 'Get-Process | Export-Csv procs.csv' },
      { name: 'Set-ExecutionPolicy', syntax: 'Set-ExecutionPolicy 策略', desc: '设置脚本执行策略', example: 'Set-ExecutionPolicy RemoteSigned' },
      { name: 'Get-Help', syntax: 'Get-Help 命令名', desc: '获取命令帮助信息', example: 'Get-Help Get-Process -Full' },
    ]
  },
  {
    name: '其他常用',
    commands: [
      { name: 'cls', syntax: 'cls', desc: '清屏', example: 'cls' },
      { name: 'echo', syntax: 'echo [文本]', desc: '显示消息或开关回显', example: 'echo Hello World' },
      { name: 'date / time', syntax: 'date /t', desc: '显示或设置日期/时间', example: 'date /t' },
      { name: 'shutdown', syntax: 'shutdown [参数]', desc: '关机或重启计算机', example: 'shutdown /r /t 60' },
      { name: 'sfc', syntax: 'sfc /scannow', desc: '系统文件检查器，修复系统文件', example: 'sfc /scannow' },
      { name: 'gpupdate', syntax: 'gpupdate [/force]', desc: '刷新组策略设置', example: 'gpupdate /force' },
      { name: 'reg', syntax: 'reg 操作 路径', desc: '注册表操作工具', example: 'reg query HKLM\\Software' },
    ]
  },
]

const filteredCategories = computed(() => {
  const keyword = search.value.trim().toLowerCase()
  if (!keyword) return categories
  return categories
    .map(cat => ({
      ...cat,
      commands: cat.commands.filter(
        cmd =>
          cmd.name.toLowerCase().includes(keyword) ||
          cmd.desc.toLowerCase().includes(keyword) ||
          cmd.syntax.toLowerCase().includes(keyword) ||
          cmd.example.toLowerCase().includes(keyword)
      )
    }))
    .filter(cat => cat.commands.length > 0)
})
</script>

<style scoped>
.windows-commands {
  min-height: 100vh;
  background: linear-gradient(135deg, #0c0c1d 0%, #1a1a3e 50%, #0c0c1d 100%);
  padding: 20px;
  color: #fff;
}

h2 {
  color: #fff;
}

.search-input {
  margin-bottom: 20px;
}

:deep(.el-input__inner) {
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  color: #fff;
}

:deep(.el-input__inner:focus) {
  border-color: #409eff;
}

:deep(.el-collapse) {
  border: none;
}

:deep(.el-collapse-item__header) {
  background: rgba(255, 255, 255, 0.05);
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  color: #fff;
}

:deep(.el-collapse-item__wrap) {
  background: transparent;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

:deep(.el-collapse-item__content) {
  background: transparent;
  color: #fff;
}

:deep(.el-table) {
  background: transparent;
  color: #fff;
}

:deep(.el-table th) {
  background: rgba(255, 255, 255, 0.1);
  color: #fff;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

:deep(.el-table td) {
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

:deep(.el-table tr) {
  background: transparent;
}

:deep(.el-table--striped .el-table__body tr.el-table__row--striped td.el-table__cell) {
  background: rgba(255, 255, 255, 0.03);
}

:deep(.el-table__empty-text) {
  color: rgba(255, 255, 255, 0.5);
}
</style>
