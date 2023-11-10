// 登录接口返回的数据类型
export interface jsonResult {
    status:string,
    message:string,
    data:any,
    totalsize:number,
    success:boolean
}

// 分页
export interface pageLimit{
    page:number,
    limit:number
}


// 单独Id
export interface singleId{
    id:number | null
}
