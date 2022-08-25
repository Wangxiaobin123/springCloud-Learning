local function get_filename(header)
    local file_name
    for _, ele in ipairs(header) do
        file_name = string.match(ele, 'filename="(.*)"')
        if file_name and file_name ~= '' then
            return file_name
        end
    end
    return nil
end

local uri = ngx.var.request_uri
path = "/usr/share/nginx/html"
path = path .. uri
local upload = require "resty.upload"

local chunk_size = 4096 -- should be set to 4096 or 8192
-- for real-world settings

local form, err = upload:new(chunk_size)

local file;
if not form then
    ngx.log(ngx.ERR, "failed to new upload: ", err)
    ngx.exit(500)
end
form:set_timeout(1000) -- 1 sec
while true do
    local typ, res, err2 = form:read()
    if not typ then
        ngx.say("failed to read: ", err2)
        return
    end
    if typ == "header" then
        local file_name = get_filename(res)
        if file_name then
            file = io.open(path .. file_name, "w+")
            if not file then
                ngx.say("failed to open file ", file_name)
                return
            end
        end
    elseif typ == "body" then
        if file then
            file:write(res)
            -- sha1:update(res)
        end
    elseif typ == "part_end" then
        file:close()
        file = nil
    elseif typ == "eof" then
        break
    else
        -- do nothing
    end
end

--local typ, res = form:read()
--ngx.say("read: ", cjson.encode({ typ, res }))