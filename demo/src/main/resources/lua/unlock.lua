-- 判断 hash set 可重入 key 的值是否等于 0
-- 如果为 0 代表 该可重入 key 不存在
--首先使用 hexists 判断 Redis Hash 表是否存给定的域。
--如果 lock 对应 Hash 表不存在，或者 Hash 表不存在 uuid 这个 key，直接返回 nil。
--若存在的情况下，代表当前锁被其持有，首先使用 hincrby使可重入次数减 1 ，然后判断计算之后可重入次数，若小于等于 0，则使用 del 删除这把锁。

if (redis.call('hexists', KEYS[1], ARGV[1]) == 0) then
    return nil;
end ;
-- 计算当前可重入次数
local counter = redis.call('hincrby', KEYS[1], ARGV[1], -1);
-- 小于等于 0 代表可以解锁
if (counter > 0) then
    return 0;
else
    redis.call('del', KEYS[1]);
    return 1;
end ;
return nil;